package com.alioth.server.domain.schedule.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.schedule.dto.req.ScheduleReqDto;
import com.alioth.server.domain.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createSchedule(@RequestBody @Valid ScheduleReqDto scheduleReqDto){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "일정이 추가되었습니다.",
                scheduleService.save(scheduleReqDto)
        );
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResponse> listSchedule(){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "일정 리스트",
                scheduleService.list()
        );
    }

    @PatchMapping("/update/{scheduleId}")
    public ResponseEntity<CommonResponse> updateSchedule(
            @RequestBody @Valid ScheduleReqDto scheduleReqDto,
            @PathVariable Long scheduleId

            ){

        log.info("로그인 유저 확인: "+ SecurityContextHolder.getContext().getAuthentication().getName());
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "일정이 수정되었습니다.",
                scheduleService.update(scheduleReqDto,scheduleId)
        );
    }

    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity<CommonResponse> deleteSchedule(@PathVariable Long scheduleId){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "일정이 삭제되었습니다.",
                scheduleService.delete(scheduleId)
        );
    }
}
