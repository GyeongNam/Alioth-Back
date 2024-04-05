package com.alioth.server.domain.excel.controller;

import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.repository.ContractRepository;
import com.alioth.server.domain.contract.service.ContractService;
import com.alioth.server.domain.dummy.domain.Custom;
import com.alioth.server.domain.excel.service.ExcelService;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final ExcelService excelService;
    private final ContractService contractService;
    private final SalesMemberService salesMemberService;
    private final TeamService teamService;
    private final ContractRepository contractRepository;

    @GetMapping(value = {"/export/{type}", "/export/{type}/{code}"})
    public void downloadContractInfo(HttpServletResponse response,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable(required = false) String type,
                                     @PathVariable(required = false) String code
    ) throws IOException, IllegalAccessException {
        SalesMembers salesMember = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
        switch (type){
            case "contract":
                contractExcel(salesMember, code, response);
                break;
            case "customerList":
                customerListExcel(salesMember, code, response);
                break;
            case "salesMembers":
                salesMembersExcel(salesMember, code, response);
                break;
        }
    }
    public void contractExcel(SalesMembers salesMember, String code, HttpServletResponse response) throws IOException, IllegalAccessException {
        switch (salesMember.getRank()){
            case HQ:
                contractExcelHq(code, response);
                break;
            case MANAGER:
                contractExcelManager(salesMember, code, response);
                break;
            case FP:
                exportExcel(response, contractList(salesMember.getSalesMemberCode().toString()));
                break;
        }
    }

    // 계약 HQ 경우 code = null, 팀 코드 , 사원 코드
    private void contractExcelHq(String code, HttpServletResponse response) throws IOException, IllegalAccessException {
        if (code == null || code.isEmpty()) {
            List<ContractResDto> allContracts = contractService.listAllContracts();
            exportExcel(response, allContracts);
        } else {
            if (Character.isLetter(code.charAt(0))){
                if (teamService.findByTeamCode(code).getDelYN().equals("N")) {
                    exportExcel(response, contractTeamList(code));
                } else {
                    throw new EntityNotFoundException("잘못된 팀이거나 삭제된 팀입니다.");
                }
            } else {
                exportExcel(response, contractList(code));
            }
        }
    }

    // 계약 Manager 로그인한 사용자가 팀이있는경우, code = null, 사원 코드
    private void contractExcelManager(SalesMembers salesMember, String code, HttpServletResponse response) throws IOException, IllegalAccessException {
        teamExist(salesMember);
        if (code == null || code.isEmpty()) {
            exportExcel(response, contractTeamList(salesMember.getTeam().getTeamCode()));
        } else if (code.matches("^[0-9]+$")/*.matches("\\d+")*/) {
            if(salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getTeam().getId()
                            .equals(salesMember.getTeam().getId())
            ){
                exportExcel(response, contractList(code));
            }
        }
    }

    public List<ContractResDto> contractList(String code) {
        return contractService.contractsByMember(
                salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getId()
        );
    }

    public List<ContractResDto> contractTeamList(String code) {
        Team team = teamService.findByTeamCode(code);
        List<ContractResDto> allTeamContracts = new ArrayList<>();
        for(SalesMembers salesMembers : team.getTeamMembers()){
            List<ContractResDto> list = contractService.contractsByMember(salesMembers.getId());
            allTeamContracts.addAll(list);
        }
        return allTeamContracts;
    }

    public void customerListExcel(SalesMembers salesMember, String code, HttpServletResponse response) throws IOException, IllegalAccessException {
        switch (salesMember.getRank()){
            case HQ:
                customerListExcelHq(code, response);
                break;
            case MANAGER:
                customerListExcelManager(salesMember, code, response);
                break;
            case FP:
                exportExcel(response, customList(salesMember.getSalesMemberCode().toString()));
                break;
        }
    }

    // 고객 HQ 일 경우 code = null, 팀 코드, 사원 코드
    private void customerListExcelHq(String code, HttpServletResponse response) throws IOException, IllegalAccessException {
        if (code == null || code.isEmpty()) {
            List<Custom> allCustomers = contractService.customTotalList();
            exportExcel(response, allCustomers);
        } else {
            if (code.matches("[a-zA-Z].*") /* Character.isLetter(code.charAt(0)) */ ) {
                if (teamService.findByTeamCode(code).getDelYN().equals("N")) {
                    exportExcel(response, customTeamList(code));
                }
            } else {
                exportExcel(response, customList(code));
            }
        }
    }

    // 고객 Manager 일 경우 code = null, 사원코드
    private void customerListExcelManager(SalesMembers salesMember, String code, HttpServletResponse response) throws IOException, IllegalAccessException {
        teamExist(salesMember);
        if (code == null || code.isEmpty()) {
            exportExcel(response, customTeamList(salesMember.getTeam().getTeamCode()));
        } else if (code.matches("^[0-9]+$") /*.matches("\\d+")*/) {
            if(salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getTeam().getId()
                    .equals(salesMember.getTeam().getId())){
                exportExcel(response, customList(code));
            }
        }
    }

    public List<Custom> customList(String code) {
        return contractService.customListByMemberId(
                salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getId()
        );
    }

    public List<Custom> customTeamList(String code) {
        Team team = teamService.findByTeamCode(code);
        List<Custom> teamCustomList = new ArrayList<>();
        for(SalesMembers salesMembers : team.getTeamMembers()){
            List<Custom> temps = contractService.customListByMemberId(salesMembers.getId());
            teamCustomList.addAll(temps);
        }
        return teamCustomList;
    }

    public void salesMembersExcel(SalesMembers salesMember, String code, HttpServletResponse response) throws IOException, IllegalAccessException {
        switch (salesMember.getRank()){
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
    private void salesMembersExcelHq(String code, HttpServletResponse response) throws IOException, IllegalAccessException {
        if (code == null || code.isEmpty()) {
            List<SalesMemberResDto> list = salesMemberService.findAll();
            exportExcel(response, list);
        } else if (code.matches("[a-zA-Z].*") /* Character.isLetter(code.charAt(0)) */) {
            Team team = teamService.findByTeamCode(code);
            if (team.getDelYN().equals("N")) {      // null 이 나올수가 없음
                List<SMTeamListResDto> memberList = teamService.findAllByTeamId(team.getId());
                exportExcel(response, memberList);
            }
            // 이부분 빠진듯
            else{
               throw new EntityNotFoundException("해체된 팀입니다.");
            }
        }
    }

    // 사원 Manager 일 경우
    private void salesMembersExcelManager(SalesMembers salesMember, HttpServletResponse response) throws IOException, IllegalAccessException {
        teamExist(salesMember);
        exportExcel(response, teamService.findAllByTeamId(salesMember.getTeam().getId()));
    }

    public <T> void exportExcel(HttpServletResponse response, List<T> list) throws IOException, IllegalAccessException {
        if (list.isEmpty()) {
            throw new NoSuchFileException("No data");
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String date = now.format(format);
        String fileDate = date.replaceAll("[-:\\s]", "");
        Workbook workbook = excelService.createExcel(list);
        String fileName = "alioth_" + fileDate + ".xlsx";
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
