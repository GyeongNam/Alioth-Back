package com.alioth.statistics.sales.controller;

import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.sales.dto.res.SalesMemberMonthResDto;
import com.alioth.statistics.sales.dto.res.SalesMemberTargetResDto;
import com.alioth.statistics.sales.dto.res.SalesMemberTotalPriceRedDto;
import com.alioth.statistics.sales.service.SalesMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class SalesController {

    private final SalesMemberService salesMemberService;

    @GetMapping("/api/stat/sales/{memberCode}/{date}")
    public ResponseEntity<CommonResponse> getPriceTargetMonth(@PathVariable Long memberCode,
                                                              @PathVariable String date) {

        List<SalesMemberMonthResDto> dto = salesMemberService.memberSalesMonth(memberCode, date);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원 매출",
                dto);
    }



    @GetMapping("/api/stat/sales/{memberCode}/{date}/price")
    public ResponseEntity<CommonResponse> getMemberPrice(@PathVariable Long memberCode,
                                                              @PathVariable String date) {

        SalesMemberTotalPriceRedDto dto = salesMemberService.memberSalesPrice(memberCode, date);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원 매출 실적",
                dto);
    }



    @GetMapping("/api/stat/sales/{memberCode}/{date}/target")
    public ResponseEntity<CommonResponse> getMemberTarget(@PathVariable Long memberCode,
                                                         @PathVariable String date) {

        Long target = salesMemberService.salesMemberTarget(memberCode, date);
        Long price = salesMemberService.memberSalesTargetResPrice(memberCode, date);

        SalesMemberTargetResDto dto = SalesMemberTargetResDto.builder()
                .target(target)
                .price(price)
                .build();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원 매출 목표",
                dto);
    }









}
