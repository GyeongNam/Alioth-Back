package com.alioth.statistics.controller;


import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.service.cencellation.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/stat")
@RestController
@RequiredArgsConstructor
public class CancellationController {

    private final CancellationService smCancellationService;
    private final CancellationService teamCancellationService;

    @GetMapping("/cancel/sm/money")
    public ResponseEntity<CommonResponse> smCancelMoneyPercent() {
        Map<?, String> result = smCancellationService.cancelMoneyPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원별 해약금액 % 입니다.",
                result
        );
    }

    @GetMapping("/cancel/sm/count")
    public ResponseEntity<CommonResponse> smCancelCountPercent() {
        Map<?, String> result = smCancellationService.cancelCountPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원별 해약건 % 입니다.",
                result
        );
    }


    @GetMapping("/cancel/team/money")
    public ResponseEntity<CommonResponse> teamCancelMoneyPercent() {
        Map<?, String> result = teamCancellationService.cancelMoneyPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "팀 해약금액 % 입니다.",
                result
        );
    }

    @GetMapping("/cancel/team/count")
    public ResponseEntity<CommonResponse> teamCancelCountPercent() {
        Map<?, String> result = teamCancellationService.cancelCountPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "팀 해약건 % 입니다.",
                result
        );
    }






}
