package com.alioth.server.domain.excel.service;

import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.service.ContractService;
import com.alioth.server.domain.excel.domain.ExcelHeaders;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final ContractService contractService;
    private final SalesMemberService salesMemeberService;
    public Workbook contractExcel(){
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<ContractResDto> contractList = contractService.listAllContracts();

        //HEADERS
        Row headerRow = sheet.createRow(0);
        Cell cell1 = headerRow.createCell(0);
        cell1.setCellValue("contractId");
        Cell cell2 = headerRow.createCell(1);
        cell2.setCellValue("contractCode");
        Cell cell3 = headerRow.createCell(2);
        cell3.setCellValue("contractDate");
        Cell cell4 = headerRow.createCell(3);
        cell4.setCellValue("contractExpireDate");
        Cell cell5 = headerRow.createCell(4);
        cell5.setCellValue("contractPeriod");
        Cell cell6 = headerRow.createCell(5);
        cell6.setCellValue("contractTotalPrice");
        Cell cell7 = headerRow.createCell(6);
        cell7.setCellValue("contractTotalPrice");
        Cell cell8 = headerRow.createCell(7);
        cell8.setCellValue("contractPaymentAmount");
        Cell cell9 = headerRow.createCell(8);
        cell9.setCellValue("contractPaymentFrequency");
        Cell cell10 = headerRow.createCell(9);
        cell10.setCellValue("contractPaymentMaturityInstallment");
        Cell cell11 = headerRow.createCell(10);
        cell11.setCellValue("contractPayer");
        Cell cell12 = headerRow.createCell(11);
        cell12.setCellValue("contractCount");
        Cell cell13 = headerRow.createCell(12);
        cell13.setCellValue("contractPaymentMethod");
        Cell cell14 = headerRow.createCell(13);
        cell14.setCellValue("contractPayer");
        Cell cell15 = headerRow.createCell(14);
        cell15.setCellValue("contractPayer");
        Cell cell16 = headerRow.createCell(15);
        cell16.setCellValue("contractConsultation");
        Cell cell17 = headerRow.createCell(16);
        cell17.setCellValue("contractStatus");
        Cell cell18 = headerRow.createCell(17);
        cell18.setCellValue("insuranceProductName");
        Cell cell19 = headerRow.createCell(18);
        cell19.setCellValue("customName");
        Cell cell20 = headerRow.createCell(19);
        cell20.setCellValue("contractMemberName");

        int rowIndex = 1;
        for (ContractResDto dto : contractList) {
            Row bodyRow = sheet.createRow(rowIndex++);

            Cell bodyCell1 = bodyRow.createCell(0);
            bodyCell1.setCellValue(dto.contractId());

            Cell bodyCell2 = bodyRow.createCell(1);
            bodyCell2.setCellValue(dto.contractCode());

            Cell bodyCell3 = bodyRow.createCell(2);
            bodyCell3.setCellValue(dto.contractDate());

            Cell bodyCell4 = bodyRow.createCell(3);
            bodyCell4.setCellValue(dto.contractExpireDate());

            Cell bodyCell5 = bodyRow.createCell(4);
            bodyCell5.setCellValue(dto.contractPeriod());

            Cell bodyCell6 = bodyRow.createCell(5);
            bodyCell6.setCellValue(dto.contractTotalPrice());

            Cell bodyCell7 = bodyRow.createCell(6);
            bodyCell7.setCellValue(dto.contractTotalPrice());

            Cell bodyCell8 = bodyRow.createCell(7);
            bodyCell8.setCellValue(dto.contractPaymentAmount());

            Cell bodyCell9 = bodyRow.createCell(8);
            bodyCell9.setCellValue(dto.contractPaymentFrequency().toString());

            Cell bodyCell10 = bodyRow.createCell(9);
            bodyCell10.setCellValue(dto.contractPaymentMaturityInstallment());

            Cell bodyCell11 = bodyRow.createCell(10);
            bodyCell11.setCellValue(dto.contractPayer());

            Cell bodyCell12 = bodyRow.createCell(11);
            bodyCell12.setCellValue(dto.contractCount());

            Cell bodyCell13 = bodyRow.createCell(12);
            bodyCell13.setCellValue(dto.contractPaymentMethod());

            Cell bodyCell14 = bodyRow.createCell(13);
            bodyCell14.setCellValue(dto.contractPayer());

            Cell bodyCell15 = bodyRow.createCell(14);
            bodyCell15.setCellValue(dto.contractPayer());

            Cell bodyCell16 = bodyRow.createCell(15);
            bodyCell16.setCellValue(dto.contractConsultation());

            Cell bodyCell17 = bodyRow.createCell(16);
            bodyCell17.setCellValue(dto.contractStatus().toString());

            Cell bodyCell18 = bodyRow.createCell(17);
            bodyCell18.setCellValue(dto.insuranceProductName());

            Cell bodyCell19 = bodyRow.createCell(18);
            bodyCell19.setCellValue(dto.customName());

            Cell bodyCell20 = bodyRow.createCell(19);
            bodyCell20.setCellValue(dto.contractMemberName());
        }
        return workbook;
    }
        public Workbook salesMembersExcel() {
            Workbook workbook = new SXSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            List<SalesMemberResDto> memberList = salesMemeberService.findAll();
            Row headerRow = sheet.createRow(0);
            Cell cell1 = headerRow.createCell(0);
            cell1.setCellValue("salesMemberCode");
            Cell cell2 = headerRow.createCell(1);
            cell2.setCellValue("name");
            Cell cell3 = headerRow.createCell(2);
            cell3.setCellValue("email");
            Cell cell4 = headerRow.createCell(3);
            cell4.setCellValue("phone");
            Cell cell5 = headerRow.createCell(4);
            cell5.setCellValue("birthDay");
            Cell cell6 = headerRow.createCell(5);
            cell6.setCellValue("address");
            Cell cell7 = headerRow.createCell(6);
            cell7.setCellValue("performanceReview");
            Cell cell8 = headerRow.createCell(7);
            cell8.setCellValue("teamCode");
            Cell cell9 = headerRow.createCell(8);
            cell9.setCellValue("teamName");
            Cell cell10 = headerRow.createCell(9);
            cell10.setCellValue("officeAddress");
            Cell cell11 = headerRow.createCell(10);
            cell11.setCellValue("extensionNumber");
            Cell cell12 = headerRow.createCell(11);
            cell12.setCellValue("rank");

            int rowIndex = 1;
            for (SalesMemberResDto dto : memberList) {
                Row bodyRow = sheet.createRow(rowIndex++);

                Cell bodyCell1 = bodyRow.createCell(0);
                bodyCell1.setCellValue(dto.salesMemberCode());

                Cell bodyCell2 = bodyRow.createCell(1);
                bodyCell2.setCellValue(dto.name());

                Cell bodyCell3 = bodyRow.createCell(2);
                bodyCell3.setCellValue(dto.email());

                Cell bodyCell4 = bodyRow.createCell(3);
                bodyCell4.setCellValue(dto.phone());

                Cell bodyCell5 = bodyRow.createCell(4);
                bodyCell5.setCellValue(dto.birthDay());

                Cell bodyCell6 = bodyRow.createCell(5);
                bodyCell6.setCellValue(dto.address());

                Cell bodyCell7 = bodyRow.createCell(6);
                bodyCell7.setCellValue(dto.performanceReview());

                Cell bodyCell8 = bodyRow.createCell(7);
                bodyCell8.setCellValue(dto.teamCode());

                Cell bodyCell9 = bodyRow.createCell(8);
                bodyCell9.setCellValue(dto.teamName());

                Cell bodyCell10 = bodyRow.createCell(9);
                bodyCell10.setCellValue(dto.officeAddress());

                Cell bodyCell11 = bodyRow.createCell(10);
                bodyCell11.setCellValue(dto.extensionNumber());

                Cell bodyCell12 = bodyRow.createCell(11);
                bodyCell12.setCellValue(dto.rank().toString());
            }
            return workbook;

    }

    public Workbook teamSalesExcel() {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<SalesMemberResDto> memberList = salesMemeberService.findAll();
        Row headerRow = sheet.createRow(0);
        Cell cell1 = headerRow.createCell(0);
        cell1.setCellValue("salesMemberCode");
        Cell cell2 = headerRow.createCell(1);
        cell2.setCellValue("name");
        Cell cell3 = headerRow.createCell(2);
        cell3.setCellValue("email");
        Cell cell4 = headerRow.createCell(3);
        cell4.setCellValue("phone");
        Cell cell5 = headerRow.createCell(4);
        cell5.setCellValue("birthDay");
        Cell cell6 = headerRow.createCell(5);
        cell6.setCellValue("address");
        Cell cell7 = headerRow.createCell(6);
        cell7.setCellValue("performanceReview");
        Cell cell8 = headerRow.createCell(7);
        cell8.setCellValue("teamCode");
        Cell cell9 = headerRow.createCell(8);
        cell9.setCellValue("teamName");
        Cell cell10 = headerRow.createCell(9);
        cell10.setCellValue("officeAddress");
        Cell cell11 = headerRow.createCell(10);
        cell11.setCellValue("extensionNumber");
        Cell cell12 = headerRow.createCell(11);
        cell12.setCellValue("rank");

        int rowIndex = 1;
        for (SalesMemberResDto dto : memberList) {
            Row bodyRow = sheet.createRow(rowIndex++);

            Cell bodyCell1 = bodyRow.createCell(0);
            bodyCell1.setCellValue(dto.salesMemberCode());

            Cell bodyCell2 = bodyRow.createCell(1);
            bodyCell2.setCellValue(dto.name());

            Cell bodyCell3 = bodyRow.createCell(2);
            bodyCell3.setCellValue(dto.email());

            Cell bodyCell4 = bodyRow.createCell(3);
            bodyCell4.setCellValue(dto.phone());

            Cell bodyCell5 = bodyRow.createCell(4);
            bodyCell5.setCellValue(dto.birthDay());

            Cell bodyCell6 = bodyRow.createCell(5);
            bodyCell6.setCellValue(dto.address());

            Cell bodyCell7 = bodyRow.createCell(6);
            bodyCell7.setCellValue(dto.performanceReview());

            Cell bodyCell8 = bodyRow.createCell(7);
            bodyCell8.setCellValue(dto.teamCode());

            Cell bodyCell9 = bodyRow.createCell(8);
            bodyCell9.setCellValue(dto.teamName());

            Cell bodyCell10 = bodyRow.createCell(9);
            bodyCell10.setCellValue(dto.officeAddress());

            Cell bodyCell11 = bodyRow.createCell(10);
            bodyCell11.setCellValue(dto.extensionNumber());

            Cell bodyCell12 = bodyRow.createCell(11);
            bodyCell12.setCellValue(dto.rank().toString());
        }
        return workbook;

    }




















    //엑셀 컬럼명 가져오기
    public List<String> getExcelHeaders(Class<Object> classes){
        List<String> headerList = new ArrayList<>();
        if (classes.isAnnotationPresent(ExcelHeaders.class)) {
            for (Field field : classes.getDeclaredFields()) {
                String headerName = field.getName(); // Default header name
                headerList.add(headerName);
                }
            }
        return headerList;
        }
    }

   /* public Workbook salesMemebersExcel(){
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<SalesMemberResDto> salesMemberList = salesMemeberService.;

        CellStyle style1 = workbook.createCellStyle();
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style1.setFillPattern(FillPatternType.BRICKS);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setBorderBottom(BorderStyle.THIN);

        //리스트 스타일 지정
        CellStyle style2 = workbook.createCellStyle();
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderBottom(BorderStyle.THIN);

        int rowIndex = 0;
        for (ContractResDto dto : contractList) {
            Row bodyRow = sheet.createRow(rowIndex++);

            Cell bodyCell1 = bodyRow.createCell(0);
            bodyCell1.setCellValue(dto.contractId());

            Cell bodyCell2 = bodyRow.createCell(1);
            bodyCell2.setCellValue(dto.contractCode());

            Cell bodyCell3 = bodyRow.createCell(2);
            bodyCell3.setCellValue(dto.contractDate());

            Cell bodyCell4 = bodyRow.createCell(3);
            bodyCell4.setCellValue(dto.contractExpireDate());

            Cell bodyCell5 = bodyRow.createCell(4);
            bodyCell5.setCellValue(dto.contractPeriod());

        }
        return workbook;
    }*/

