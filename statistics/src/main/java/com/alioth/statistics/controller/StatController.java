package com.alioth.statistics.controller;

import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.domain.batch.dto.res.*;
import com.alioth.statistics.service.stat.StatHQSalesService;
import com.alioth.statistics.service.stat.StatMemberSalesService;
import com.alioth.statistics.service.stat.StatProductService;
import com.alioth.statistics.service.stat.StatTeamSalesService;
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
public class StatController {

    private final StatMemberSalesService batchStatService;
    private final StatTeamSalesService teamSalesService;
    private final StatHQSalesService hqSalesService;
    private final StatProductService productService;


    @GetMapping("/api/batch/sales-member")
    public ResponseEntity<CommonResponse> getMemberSales() {
        List<BatchMemberSalesResDto> dto = batchStatService.getMemberSales();

        return CommonResponse.responseMessage(HttpStatus.OK, "사원 일별 결과입니다", dto);
    }

    @GetMapping("/api/batch/sales-team/day")
    public ResponseEntity<CommonResponse> getTeamSalesDay() {
        List<BatchTeamSalesResDto> dto = teamSalesService.teamSalesDay();

        return CommonResponse.responseMessage(HttpStatus.OK, "팀 일별 결과입니다", dto);
    }

    @GetMapping("/api/batch/sales-team/month")
    public ResponseEntity<CommonResponse> getTeamSalesMonth() {
        List<BatchTeamSalesResDto> dto = teamSalesService.teamSalesMonth();

        return CommonResponse.responseMessage(HttpStatus.OK, "팀 월별 결과입니다", dto);
    }

    @GetMapping("/api/batch/sales-team/quarter")
    public ResponseEntity<CommonResponse> getTeamSalesQuarter() {
        List<BatchTeamSalesResDto> dto = teamSalesService.teamSalesQuarter();

        return CommonResponse.responseMessage(HttpStatus.OK, "팀 반기별 결과입니다", dto);
    }

    @GetMapping("/api/batch/sales-team/year")
    public ResponseEntity<CommonResponse> getTeamSalesYear() {
        List<BatchTeamSalesResDto> dto = teamSalesService.teamSalesYear();

        return CommonResponse.responseMessage(HttpStatus.OK, "팀 년별 결과입니다", dto);
    }








    @GetMapping("/api/batch/sales-hq/day")
    public ResponseEntity<CommonResponse> getHQSalesDay() {
        List<BatchHQSalesResDto> dto = hqSalesService.hqSalesDay();

        return CommonResponse.responseMessage(HttpStatus.OK, "전사 일별 결과입니다", dto);
    }

    @GetMapping("/api/batch/sales-hq/month")
    public ResponseEntity<CommonResponse> getHQSalesMonth() {
        List<BatchHQSalesResDto> dto = hqSalesService.hqSalesMonth();

        return CommonResponse.responseMessage(HttpStatus.OK, "전사 월별 결과입니다", dto);
    }

    @GetMapping("/api/batch/sales-hq/quarter")
    public ResponseEntity<CommonResponse> getHQSalesQuarter() {
        List<BatchHQSalesResDto> dto = hqSalesService.hqSalesQuarter();

        return CommonResponse.responseMessage(HttpStatus.OK, "전사 반기 결과입니다", dto);
    }

    @GetMapping("/api/batch/sales-hq/year")
    public ResponseEntity<CommonResponse> getHQSalesYear() {
        List<BatchHQSalesResDto> dto = hqSalesService.hqSalesYear();

        return CommonResponse.responseMessage(HttpStatus.OK, "전사 연별 결과입니다", dto);
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
