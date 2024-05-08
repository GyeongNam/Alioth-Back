package com.alioth.statistics.dashboard.controller;


import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.dashboard.dto.res.DashboardBestTeamResDto;
import com.alioth.statistics.dashboard.dto.res.DashboardGodResDto;
import com.alioth.statistics.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/statistics/api/dashboard/god")
    public ResponseEntity<CommonResponse> getSalesGod() {
        DashboardGodResDto dto = dashboardService.getSalesGod();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "보험의 신",
                dto
        );
    }

    @GetMapping("/statistics/api/dashboard/best-team")
    public ResponseEntity<CommonResponse> getBestTeam() {
        DashboardBestTeamResDto dto = dashboardService.getBestTeam();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "최우수 고과 팀",
                dto
        );
    }


}
