package com.alioth.server.domain.excel.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.service.ContractService;
import com.alioth.server.domain.dummy.domain.ContractStatus;
import com.alioth.server.domain.dummy.domain.Custom;
import com.alioth.server.domain.excel.dto.ExcelReqDto;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.res.SMTeamListResDto;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelService {
    private final SalesMemberService salesMemberService;
    private final TeamService teamService;
    private final ContractService contractService;

    public <T> Workbook createExcel(List<T> list) throws IllegalAccessException {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row headerRow = sheet.createRow(0);
        int k = 0;
        for (Field field : list.getFirst().getClass().getDeclaredFields()) {
            Cell cell = headerRow.createCell(k++);
            cell.setCellValue(field.getName());
        }

        int i = 1;
        for (Object object : list) {
            Row row = sheet.createRow(i++);
            int j = 0;
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Cell cell = row.createCell(j++);
                if (field.get(object) != null) {
                    cell.setCellValue(field.get(object).toString());
                } else {
                    cell.setCellValue("");
                }
            }
        }
        return workbook;
    }

    public void contractExcel(SalesMembers salesMember, String code, String status, HttpServletResponse response, ExcelReqDto dto
    ) throws IOException, IllegalAccessException {
        switch (salesMember.getRank()) {
            case HQ:
                contractExcelHq(code, status, response, dto);
                break;
            case MANAGER:
                contractExcelManager(salesMember, code, status, response, dto);
                break;
            case FP:
                exportExcel(response, contractList(salesMember.getSalesMemberCode().toString(), status, dto));
                break;
        }
    }

    // 계약 HQ 경우 code = null, 팀 코드 , 사원 코드
    public void contractExcelHq(String code, String status, HttpServletResponse response, ExcelReqDto dto
    ) throws IOException, IllegalAccessException {
        if (code == null || code.isEmpty()) {
            switch (status) {
                case null:
                    exportExcel(response, contractService.findAllContractsByPeriod(dto).stream().toList());
                    break;
                case "New":
                    exportExcel(response, contractService.findAllContractsByPeriod(dto)
                            .stream().filter(contractResDto -> contractResDto.contractStatus().equals(ContractStatus.New))
                            .toList());
                    break;
                case "Renewals":
                    exportExcel(response, contractService.findAllContractsByPeriod(dto)
                            .stream().filter(contractResDto -> contractResDto.contractStatus().equals(ContractStatus.Renewals))
                            .toList());
                    break;
                case "Cancellation":
                    exportExcel(response, contractService.findAllContractsByPeriod(dto)
                            .stream().filter(contractResDto -> contractResDto.contractStatus().equals(ContractStatus.Cancellation))
                            .toList());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + status);
            }
            ;
        } else {
            if (Character.isLetter(code.charAt(0))) {
                if (teamService.findByTeamCode(code).getDelYN().equals("N")) {
                    exportExcel(response, contractTeamList(code, status, dto));
                } else {
                    throw new EntityNotFoundException("잘못된 팀이거나 삭제된 팀입니다.");
                }
            } else {
                exportExcel(response, contractList(code, status, dto));
            }
        }
    }

    // 계약 Manager 로그인한 사용자가 팀이있는경우, code = null, 사원 코드
    public void contractExcelManager(SalesMembers salesMember, String code, String status, HttpServletResponse response, ExcelReqDto dto
    ) throws IOException, IllegalAccessException {
        teamExist(salesMember);
        if (code == null || code.isEmpty()) {
            exportExcel(response, contractTeamList(salesMember.getTeam().getTeamCode(), status, dto));
        } else if (code.matches("\\d+")) {
            if (salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getTeam().getId()
                    .equals(salesMember.getTeam().getId())
            ) {
                exportExcel(response, contractList(code, status, dto));
            }
        }
    }

    public List<ContractResDto> contractList(String code, String status, ExcelReqDto dto) {
        Long memberId = salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getId();
        return switch (status) {
            case null -> contractService.allContractsByMemberAndPeriod(memberId, dto);
            case "New" -> contractService.allContractsByMemberAndPeriod(memberId, dto)
                    .stream().filter(contractResDto -> contractResDto.contractStatus().equals(ContractStatus.New))
                    .toList();
            case "Renewals" -> contractService.allContractsByMemberAndPeriod(memberId, dto)
                    .stream().filter(contractResDto -> contractResDto.contractStatus().equals(ContractStatus.Renewals))
                    .toList();
            case "Cancellation" -> contractService.allContractsByMemberAndPeriod(memberId, dto)
                    .stream().filter(contractResDto -> contractResDto.contractStatus().equals(ContractStatus.Cancellation))
                    .toList();
            default -> throw new IllegalStateException("잘못된 요청입니다.");
        };
    }

    public List<ContractResDto> contractTeamList(String code, String status, ExcelReqDto dto) {
        List<SalesMembers> teamMembers = teamService.findByTeamCode(code).getTeamMembers();
        return switch (status) {
            case null -> teamMembers.stream()
                        .flatMap(member -> contractService.allContractsByMemberAndPeriod(member.getId(), dto).stream())
                        .collect(Collectors.toList());
            case "New" -> teamMembers.stream()
                    .flatMap(member -> contractService.allContractsByMemberAndPeriod(member.getId(), dto).stream()
                            .filter(contractResDto -> contractResDto.contractStatus().equals(ContractStatus.New)))
                    .collect(Collectors.toList());
            case "Renewals" -> teamMembers.stream()
                    .flatMap(member -> contractService.allContractsByMemberAndPeriod(member.getId(), dto).stream()
                            .filter(contractResDto -> contractResDto.contractStatus().equals(ContractStatus.Renewals)))
                    .collect(Collectors.toList());
            case "Cancellation" -> teamMembers.stream()
                    .flatMap(member -> contractService.allContractsByMemberAndPeriod(member.getId(), dto).stream()
                            .filter(contractResDto -> contractResDto.contractStatus().equals(ContractStatus.Cancellation)))
                    .collect(Collectors.toList());
            default -> throw new IllegalStateException("잘못된 요청입니다.");
        };
    }


    public void customerListExcel(SalesMembers salesMember, String code, HttpServletResponse response, ExcelReqDto dto
    ) throws IOException, IllegalAccessException {
        switch (salesMember.getRank()) {
            case HQ:
                customerListExcelHq(code, response, dto);
                break;
            case MANAGER:
                customerListExcelManager(salesMember, code, response, dto);
                break;
            case FP:
                exportExcel(response, customList(salesMember.getSalesMemberCode().toString(), dto));
                break;
        }
    }


    // 고객 HQ 일 경우 code = null, 팀 코드, 사원 코드
    private void customerListExcelHq(String code, HttpServletResponse response, ExcelReqDto dto
    ) throws IOException, IllegalAccessException {
        if (code == null || code.isEmpty()) {
            List<Custom> allCustomers = contractService.customTotalList(dto);
            exportExcel(response, allCustomers);
        } else {
            if (Character.isLetter(code.charAt(0))) {
                if (teamService.findByTeamCode(code).getDelYN().equals("N")) {
                    exportExcel(response, customTeamList(code, dto));
                }
            } else {
                exportExcel(response, customList(code, dto));

            }
        }
    }

    // 고객 Manager 일 경우 code = null, 사원코드
    private void customerListExcelManager(SalesMembers salesMember, String code, HttpServletResponse response, ExcelReqDto dto
    ) throws IOException, IllegalAccessException {
        teamExist(salesMember);
        if (code == null || code.isEmpty()) {
            exportExcel(response, customTeamList(salesMember.getTeam().getTeamCode(), dto));
        } else if (code.matches("\\d+")) {
            if (salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getTeam().getId()
                    .equals(salesMember.getTeam().getId())) {
                exportExcel(response, customList(code, dto));
            }
        }
    }

    public List<Custom> customList(String code, ExcelReqDto dto) {
        return contractService.customListByMemberId(
                salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getId(),
                dto
        );
    }

    public List<Custom> customTeamList(String code, ExcelReqDto dto) {
        List<Custom> customers = new ArrayList<>();
        for (SalesMembers member : teamService.findByTeamCode(code).getTeamMembers()) {
            customers.addAll(contractService.customListByMemberId(member.getId(), dto));
        }
        return customers;
    }

    public void salesMembersExcel(SalesMembers salesMember, String code, HttpServletResponse response
    ) throws IOException, IllegalAccessException {

        switch (salesMember.getRank()) {
            case HQ:
                salesMembersExcelHq(code, response);
                break;
            case MANAGER:
                salesMembersExcelManager(salesMember, response);
                break;
            case FP:
                throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

    // 사원 HQ 일 경우  code = null , 팀 코드
    private void salesMembersExcelHq(String code, HttpServletResponse response
    ) throws IOException, IllegalAccessException {
        if (code == null || code.isEmpty()) {
            List<SalesMemberResDto> list = salesMemberService.findAll();
            exportExcel(response, list);
        } else if (Character.isLetter(code.charAt(0))) {
            Team team = teamService.findByTeamCode(code);
            if (team.getDelYN().equals("N")) {
                List<SMTeamListResDto> memberList = teamService.findAllByTeamCode(code);
                exportExcel(response, memberList);
            } else {
                throw new EntityNotFoundException("해체된 팀입니다.");
            }
        } else {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }
    }

    // 사원 Manager 일 경우
    private void salesMembersExcelManager(SalesMembers salesMember, HttpServletResponse response
    ) throws IOException, IllegalAccessException {
        teamExist(salesMember);
        exportExcel(response, teamService.findAllByTeamCode(salesMember.getTeam().getTeamCode()));
    }

    public <T> void exportExcel(HttpServletResponse response, List<T> list) throws IOException, IllegalAccessException {
        if (list.isEmpty()) {
            throw new NoSuchFileException("No data");
        }
        LocalDateTime now = LocalDateTime.now();
        String dateFormat = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        Workbook workbook = this.createExcel(list);
        String fileName = "alioth_" + dateFormat + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void teamExist(SalesMembers salesMember) throws AccessDeniedException {
        if (salesMember.getTeam() == null || salesMember.getTeam().getDelYN().equals("Y")) {
            throw new AccessDeniedException("잘못된 접근입니다.");
        }
    }
}