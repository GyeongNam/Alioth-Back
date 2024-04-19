package com.alioth.statistics.controller;

import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.domain.batch.dto.res.BatchMemberSalesResDto;
import com.alioth.statistics.domain.batch.dto.res.BatchProductCountResDto;
import com.alioth.statistics.domain.batch.dto.res.BatchProductPriceResDto;
import com.alioth.statistics.service.batch.BatchStatMemberSalesService;
import com.alioth.statistics.service.batch.BatchStatProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class BatchStatController {

    private final BatchStatMemberSalesService batchStatService;
    private final BatchStatProductService productService;


    @GetMapping("/api/batch/sales-member")
    public ResponseEntity<CommonResponse> getMemberSales() {
        List<BatchMemberSalesResDto> dto = batchStatService.getMemberSales();

        return CommonResponse.responseMessage(HttpStatus.OK, "사원 일별 결과입니다", dto);
    }




    @GetMapping("/api/batch/contract-rank/price")
    public ResponseEntity<CommonResponse> getRankProductsPrice() {
        List<BatchProductPriceResDto> dto = productService.productDayPrice();

        return CommonResponse.responseMessage(HttpStatus.OK, "보험별 일간 금액별 결과입니다", dto);
    }


    @GetMapping("/api/batch/contract-rank/count")
    public ResponseEntity<CommonResponse> getRankProductsCount() {
        List<BatchProductCountResDto> dto = productService.productDayCount();

        return CommonResponse.responseMessage(HttpStatus.OK, "보험별 일간 금액건 결과입니다", dto);
    }



}
