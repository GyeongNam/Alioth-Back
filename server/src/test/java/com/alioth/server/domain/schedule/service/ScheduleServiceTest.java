package com.alioth.server.domain.schedule.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.domain.ScheduleType;
import com.alioth.server.domain.schedule.dto.req.ScheduleCreateDto;

import com.alioth.server.domain.schedule.dto.res.ScheduleResDto;
import com.alioth.server.domain.schedule.dto.req.ScheduleUpdateDto;
import com.alioth.server.domain.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TypeChange typeChange;

    private Schedule schedule;

    @BeforeEach
    void setUp() {
        ScheduleCreateDto scheduleCreateDto = ScheduleCreateDto.builder()
                .scheduleStartTime(LocalDateTime.now())
                .scheduleEndTime(LocalDateTime.now().plusHours(1))
                .scheduleNote("Test Note")
                .scheduleType(ScheduleType.MEETING)
                .allDay("1")
                .build();

        ScheduleResDto savedScheduleResDto = scheduleService.save(scheduleCreateDto);
        this.schedule = scheduleRepository.findById(savedScheduleResDto.scheduleId())
                .orElseThrow(() -> new EntityNotFoundException("Saved schedule not found"));
    }

    @Test
    @DisplayName("일정 저장 테스트")
    void save() {
        assertNotNull(schedule.getScheduleId());
    }

    @Test
    @DisplayName("일정 수정 테스트")
    void update() {
        ScheduleUpdateDto scheduleUpdateDto = ScheduleUpdateDto.builder()
                .scheduleId(schedule.getScheduleId())
                .scheduleStartTime(schedule.getScheduleStartTime())
                .scheduleEndTime(schedule.getScheduleEndTime())
                .scheduleNote("Updated Note")
                .scheduleType(schedule.getScheduleType())
                .allDay(schedule.getAllDay())
                .build();

        ScheduleResDto updatedSchedule = scheduleService.update(scheduleUpdateDto);
        assertEquals("Updated Note", updatedSchedule.scheduleNote());
    }

    @Test
    @DisplayName("일정 삭제 테스트")
    void delete() {
        ScheduleResDto deletedSchedule = scheduleService.delete(schedule.getScheduleId());
        assertEquals("Y", deletedSchedule.del_yn());
    }

    @Test
    @DisplayName("특정 사용자의 모든 일정 조회 테스트")
    void list() {
        List<ScheduleResDto> schedules = scheduleService.list();
        assertFalse(schedules.isEmpty());
    }
}