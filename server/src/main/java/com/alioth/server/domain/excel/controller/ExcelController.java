package com.alioth.server.domain.excel.controller;

import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.service.ContractService;
import com.alioth.server.domain.excel.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final ExcelService excelService;
    @GetMapping("/download/contract")
    public void downloadContractInfo(HttpServletResponse response) throws IOException {
        Workbook workbook = excelService.contractExcel();
        String fileName = "contracts.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }


    @GetMapping("/download/salesmembers")
    public void downloadSalesMemebersInfo(HttpServletResponse response) throws IOException {
        Workbook workbook = excelService.salesMembersExcel();
        String fileName = "salesMembers.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }


    @GetMapping("/download/teamsales")
    public void downloadTeamSalesInfo(HttpServletResponse response) throws IOException {
        Workbook workbook = excelService.salesMembersExcel();
        String fileName = "TeamSalesInfo.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}





