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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

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
            List<ContractResDto> contractList = contractService.listAllContracts();
            if(!contractList.isEmpty()){
                Workbook workbook = excelService.createExcel(contractList);
                String fileName = "contracts.xlsx";
                sendExcel(fileName,response,workbook);
            } else {
                throw new NoSuchElementException("No contracts");
            }
        }

        if(sm.getRank() == SalesMemberType.MANAGER){
            if(sm.getTeam().getId() == null || sm.getTeam().getDelYN().equals("Y")){
                throw new Exception("Illegal access");
            } else {
            List<SalesMemberResDto> teamMembers = salesMemberService.findAllMembersByTeamId(sm.getTeam().getId());
            try{
                List<ContractResDto> allTeamContracts = new ArrayList<>();
                for(SalesMemberResDto dto : teamMembers) {
                    SalesMembers m = salesMemberService.findBySalesMemberCode(dto.salesMemberCode());
                    List<ContractResDto> list = contractService.contractsByMember(m.getId());
                    allTeamContracts.addAll(list);
                }
                Workbook workbook = excelService.createExcel(allTeamContracts);
                String fileName = "teamContracts.xlsx";
                sendExcel(fileName,response,workbook);
            } catch (NoSuchElementException e){
                log.error("error message" + e.getMessage());
                }
            }
        }

        if(sm.getRank() == SalesMemberType.FP){
            List<ContractResDto> contractsByMember = contractService.contractsByMember(sm.getId());
            if(!contractsByMember.isEmpty()){
                Workbook workbook = excelService.createExcel(contractsByMember);
                String fileName = "myContracts.xlsx";
                sendExcel(fileName,response,workbook);
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
                Workbook workbook = excelService.createExcel(memberList);
                String fileName = "teamSalesMembers.xlsx";
                sendExcel(fileName,response,workbook);
            } else {
                throw new NoSuchElementException("No data");
            }
        }
// 코드 리뷰: 여기 예외처리 해야할까요?
        if(sm.getRank() == SalesMemberType.HQ){
            List<SalesMemberResDto> list = salesMemberService.findAll();
            Workbook workbook = excelService.createExcel(list);
            String fileName = "salesMembers.xlsx";
            sendExcel(fileName,response,workbook);
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
                Workbook workbook = excelService.createExcel(myCustomList);
                String fileName = "myCustomers.xlsx";
                sendExcel(fileName, response, workbook);
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
                Workbook workbook = excelService.createExcel(teamCustomList);
                String fileName = "teamCustomers.xlsx";
                sendExcel(fileName,response,workbook);
            } catch (NoSuchElementException e){
                log.error("error message: " + e.getMessage());
            }
        }

        if(sm.getRank() == SalesMemberType.HQ) {
            List<Custom> allCustoms = contractService.customTotalList();
            try{
                Workbook workbook = excelService.createExcel(allCustoms);
                String fileName = "allCustomers.xlsx";
                sendExcel(fileName,response,workbook);
            } catch (NoSuchElementException e){
                log.error("error message: " +  e.getMessage());
            }
        }
    }

    //매출


    public void sendExcel(String fileName, HttpServletResponse response,Workbook workbook) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    @GetMapping("/test")
    public List<Contract> test(){
        SalesMembers sm = salesMemberService.findBySalesMemberCode(2024312L);
        return contractRepository.findAllBySalesMembersId(sm.getId());
    }

 /*   public <T> void exportExcel(String fileName, HttpServletResponse response, List<T> list) throws IOException, IllegalAccessException {
        Workbook workbook = excelService.createExcel(list);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }*/
}





