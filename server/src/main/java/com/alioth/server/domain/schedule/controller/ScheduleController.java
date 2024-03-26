package com.alioth.server.domain.schedule.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.schedule.dto.req.ScheduleCreateDto;
import com.alioth.server.domain.schedule.dto.req.ScheduleUpdateDto;
import com.alioth.server.domain.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    ScheduleController (
            ScheduleService scheduleService
    ){
        this.scheduleService = scheduleService;
    }


    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createSchedule(@RequestBody @Valid ScheduleCreateDto scheduleCreateDto){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "일정이 추가되었습니다.",
                scheduleService.save(scheduleCreateDto)
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

    @PatchMapping("/update")
    public ResponseEntity<CommonResponse> updateSchedule(@RequestBody @Valid ScheduleUpdateDto scheduleUpdateDto){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "일정이 수정되었습니다.",
                scheduleService.update(scheduleUpdateDto)
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
