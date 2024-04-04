package com.alioth.server.domain.excel.service;

import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.service.ContractService;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.service.SalesMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelService {
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
            int j = 0 ;
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Cell cell = row.createCell(j++);
                if(field.get(object)!= null){
                    cell.setCellValue(field.get(object).toString());
                } else {
                    cell.setCellValue("");
                }
            }
        }
        return workbook;
    }
}
