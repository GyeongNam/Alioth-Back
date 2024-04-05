package com.alioth.server.domain.excel.controller;

import com.alioth.server.domain.contract.domain.Contract;
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
import com.alioth.server.domain.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.checkerframework.checker.units.qual.N;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
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
    @GetMapping("/contract")
    public void downloadContractInfo(HttpServletResponse response,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam(value = "memberId", required = false) Long memberId
    ) throws Exception {
        //findBySalesMemberCode : EntityException | 표기?
        SalesMembers sm = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
//        SalesMembers sm = salesMemberService.findBySalesMemberCode(202437L);
        if(sm.getRank() == SalesMemberType.HQ) {
            if (memberId != null) {
                SalesMembers member = salesMemberService.findBySalesMemberCode(memberId);
                List<ContractResDto> memberContracts = contractService.contractsByMember(member.getId());
                exportExcel(response, memberContracts);
            } else {
                List<ContractResDto> contractList = contractService.listAllContracts();
                if(!contractList.isEmpty()){
                    exportExcel(response,contractList);
                } else {
                    throw new NoSuchElementException("No contracts");
                }
            }
        }

        if(sm.getRank() == SalesMemberType.MANAGER){
            if(sm.getTeam().getId() == null || sm.getTeam().getDelYN().equals("Y")){
                throw new Exception("Illegal access");
            } else {
                // 사원 개개인 별(본인 포함) 엑셀 다운
                if (memberId != null) {
                    SalesMembers member = salesMemberService.findBySalesMemberCode(memberId);
                    List<ContractResDto> memberContracts = contractService.contractsByMember(member.getId());
                    exportExcel(response, memberContracts);
                } else {
                    // 팀 전체 계약 리스트
                    List<SalesMemberResDto> teamMembers = salesMemberService.findAllMembersByTeamId(sm.getTeam().getId());
                    try {
                        List<ContractResDto> allTeamContracts = new ArrayList<>();
                        for (SalesMemberResDto dto : teamMembers) {
                            SalesMembers m = salesMemberService.findBySalesMemberCode(dto.salesMemberCode());
                            List<ContractResDto> list = contractService.contractsByMember(m.getId());
                            allTeamContracts.addAll(list);
                        }
                        exportExcel(response, allTeamContracts);
                    } catch (NoSuchElementException e) {
                        log.error("error message" + e.getMessage());
                    }
                }
            }
        }

        if(sm.getRank() == SalesMemberType.FP){
            List<ContractResDto> contractsByMember = contractService.contractsByMember(sm.getId());
            if(!contractsByMember.isEmpty()){
                exportExcel(response,contractsByMember);
            } else {
                throw new NoSuchElementException("No contracts");
            }
        }
    }


    //사원 리스트
    @GetMapping("/salesmembers")
    public void downloadSalesMembersInfo(HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) throws IOException, IllegalAccessException {
        SalesMembers sm = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
//        SalesMembers sm = salesMemberService.findBySalesMemberCode(202437L);
        if(sm.getRank() == SalesMemberType.FP){
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        if(sm.getRank() == SalesMemberType.MANAGER){
            if(!sm.getTeam().getTeamMembers().isEmpty()){
                List<SMTeamListResDto> memberList = teamService.findAllByTeamId(sm.getTeam().getId());
                exportExcel(response,memberList);
            } else {
                throw new NoSuchElementException("No data");
            }
        }
// 코드 리뷰: 여기 예외처리 해야할까요?
        if(sm.getRank() == SalesMemberType.HQ){
            List<SalesMemberResDto> list = salesMemberService.findAll();
            exportExcel(response,list);
        }
    }


    //고객 리스트
    @GetMapping("/customerlist")
    public void downloadCustomerByMember(HttpServletResponse response/*, @AuthenticationPrincipal UserDetails userDetails*/) throws IOException, IllegalAccessException {
//        SalesMembers sm = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
        SalesMembers sm = salesMemberService.findBySalesMemberCode(2024314L);
        if(sm.getRank() == SalesMemberType.FP) {
            List<Custom> myCustomList = contractService.customList(sm.getId());
            try {
                exportExcel(response, myCustomList);
            } catch (NoSuchElementException e) {
                log.error("error message: " + e.getMessage());
            }
        }

        if(sm.getRank() == SalesMemberType.MANAGER) {
            List<SalesMemberResDto> teamMembers = salesMemberService.findAllMembersByTeamId(sm.getTeam().getId());
            List<Custom> teamCustomList = new ArrayList<>();
            for(SalesMemberResDto dto : teamMembers){
//                SalesMembers member = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
                SalesMembers member = salesMemberService.findBySalesMemberCode(dto.salesMemberCode());
                List<Custom> temps = contractService.customList(member.getId());
                teamCustomList.addAll(temps);
            }
            try{
                exportExcel(response,teamCustomList);
            } catch (NoSuchElementException e){
                log.error("error message: " + e.getMessage());
            }
        }

        if(sm.getRank() == SalesMemberType.HQ) {
            List<Custom> allCustoms = contractService.customTotalList();
            try{
                exportExcel(response,allCustoms);
            } catch (NoSuchElementException e){
                log.error("error message: " +  e.getMessage());
            }
        }
    }

    //매출

// 공통화 메서드
    public <T> void exportExcel(HttpServletResponse response, List<T> list) throws IOException, IllegalAccessException {
        String shortUUID = UUID.randomUUID().toString().substring(0, 6);
        Workbook workbook = excelService.createExcel(list);
        String fileName = "contracts_" + shortUUID + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }



}





