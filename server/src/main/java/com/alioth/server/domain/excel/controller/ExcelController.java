package com.alioth.server.domain.excel.controller;

import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.repository.ContractRepository;
import com.alioth.server.domain.contract.service.ContractService;
import com.alioth.server.domain.dummy.domain.Custom;
import com.alioth.server.domain.excel.service.ExcelService;
import com.alioth.server.domain.member.domain.SalesMemberType;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    //계약
    @GetMapping(value = {"/contract", "/contract/{code}"})
    public void downloadContractInfo(HttpServletResponse response,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable(required = false) String code
    ) throws IOException, IllegalAccessException {
        SalesMembers salesMember = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
        if (salesMember.getRank() == SalesMemberType.HQ) {
            if (code == null || code.isEmpty()) {
                List<ContractResDto> allContracts = contractService.listAllContracts();
                exportExcel(response, allContracts);
            } else {
                if (code.matches("[a-zA-Z].*")) {
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
        if (salesMember.getRank() == SalesMemberType.MANAGER) {
            if (salesMember.getTeam() == null || salesMember.getTeam().getDelYN().equals("Y")) {
                throw new AccessDeniedException("Illegal access");
            }
            if (code == null || code.isEmpty()) {
                exportExcel(response, contractTeamList(salesMember.getTeam().getTeamCode()));
            } else if (code.matches("^[0-9]+$")) {
                if(salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getTeam().getId()
                        .equals(salesMember.getTeam().getId())){
                    exportExcel(response, contractList(code));
                }
            }
        }
        if (salesMember.getRank() == SalesMemberType.FP) {
            exportExcel(response, contractList(userDetails.getUsername()));

        }
    }

    //고객 리스트
    @GetMapping(value = {"/customerList", "/customerList/{code}"})
    public void downloadCustomerByMember(HttpServletResponse response,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable(required = false) String code
    ) throws IOException, IllegalAccessException {
        SalesMembers salesMember = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
        if (salesMember.getRank() == SalesMemberType.HQ) {
            if (code == null || code.isEmpty()) {
                List<Custom> allCustomers = contractService.customTotalList();
                exportExcel(response, allCustomers);
            } else {
                if (code.matches("[a-zA-Z].*")) {
                    if (teamService.findByTeamCode(code).getDelYN().equals("N")) {
                        exportExcel(response, customTeamList(code));
                    }
                } else {
                    exportExcel(response, customList(code));
                }
            }
        }
        if (salesMember.getRank() == SalesMemberType.MANAGER) {
            if (salesMember.getTeam() == null || salesMember.getTeam().getDelYN().equals("Y")) {
                throw new AccessDeniedException("Illegal access");
            }
            if (code == null || code.isEmpty()) {
                exportExcel(response, customTeamList(salesMember.getTeam().getTeamCode()));
            } else if (code.matches("^[0-9]+$")) {
                if(salesMemberService.findBySalesMemberCode(Long.parseLong(code)).getTeam().getId()
                        .equals(salesMember.getTeam().getId())){
                exportExcel(response, customList(code));
                }
            }
        }
        if (salesMember.getRank() == SalesMemberType.FP) {
            exportExcel(response, customList(userDetails.getUsername()));
        }
    }


    //사원 리스트
    @GetMapping(value = {"/salesmembers","/salesmembers/{code}"})
    public void downloadSalesMembersInfo(HttpServletResponse response,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable(required = false) String code
    ) throws IOException, IllegalAccessException {
        SalesMembers salesMember = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
        if (salesMember.getRank() == SalesMemberType.FP) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        if (salesMember.getRank() == SalesMemberType.HQ) {
            if (code == null || code.isEmpty()) {
                List<SalesMemberResDto> list = salesMemberService.findAll();
                exportExcel(response, list);
            } else if (code.matches("[a-zA-Z].*")) {
                Team team = teamService.findByTeamCode(code);
                if (team != null && team.getDelYN().equals("N")) {
                    List<SMTeamListResDto> memberList = teamService.findAllByTeamId(team.getId());
                    exportExcel(response, memberList);
                }
            }
        }
        if (salesMember.getRank() == SalesMemberType.MANAGER) {
            if (salesMember.getTeam() == null || salesMember.getTeam().getDelYN().equals("Y")) {
                throw new AccessDeniedException("Illegal access");
            }
            List<SMTeamListResDto> memberList = teamService.findAllByTeamId(salesMember.getTeam().getId());
            exportExcel(response, memberList);
        }
    }

   //매출

    // 공통화 메서드
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


    //계약 관련 공통화 메서드
    public List<ContractResDto> contractList(String code) {
        SalesMembers member = salesMemberService.findBySalesMemberCode(Long.parseLong(code));
        return contractService.contractsByMember(member.getId());
    }

    public List<ContractResDto> contractTeamList(String code) {
        Team team = teamService.findByTeamCode(code);
        List<SalesMemberResDto> teamMembers = salesMemberService.findAllMembersByTeamId(team.getId());
        List<ContractResDto> allTeamContracts = new ArrayList<>();
        for (SalesMemberResDto dto : teamMembers) {
            SalesMembers m = salesMemberService.findBySalesMemberCode(dto.salesMemberCode());
            List<ContractResDto> list = contractService.contractsByMember(m.getId());
            allTeamContracts.addAll(list);
        }
        return allTeamContracts;
    }

    //고객 리스트 관련 공통화 메서드
    public List<Custom> customList(String code) {
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(Long.parseLong(code));
        return contractService.customListByMemberId(salesMembers.getId());
    }

    public List<Custom> customTeamList(String code) {
        Team team = teamService.findByTeamCode(code);
        List<SalesMemberResDto> teamMembers = salesMemberService.findAllMembersByTeamId(team.getId());
        List<Custom> teamCustomList = new ArrayList<>();
        for (SalesMemberResDto dto : teamMembers) {
            SalesMembers member = salesMemberService.findBySalesMemberCode(dto.salesMemberCode());
            List<Custom> temps = contractService.customListByMemberId(member.getId());
            teamCustomList.addAll(temps);
        }
        return teamCustomList;
    }
}






