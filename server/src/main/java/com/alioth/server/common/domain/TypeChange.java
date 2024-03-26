package com.alioth.server.common.domain;

import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.dto.ScheduleCreateDto;
import com.alioth.server.domain.schedule.dto.ScheduleResDto;
import com.alioth.server.domain.schedule.dto.ScheduleUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class TypeChange {

    public Schedule ScheduleCreateDtoToSchedule(ScheduleCreateDto scheduleCreateDto){
        return Schedule.builder()
                .scheduleStartTime(scheduleCreateDto.scheduleStartTime())
                .scheduleEndTime(scheduleCreateDto.scheduleEndTime())
                .scheduleNote(scheduleCreateDto.scheduleNote())
                .scheduleType(scheduleCreateDto.scheduleType())
                .allDay(scheduleCreateDto.allDay())
                .memberId(1L) // 사원 id
                .build();
    }

    public ScheduleResDto scheduleToScheduleResDto(Schedule schedule){
        return ScheduleResDto.builder()
                .scheduleId(schedule.getScheduleId())
                .scheduleStartTime(schedule.getScheduleStartTime())
                .scheduleEndTime(schedule.getScheduleEndTime())
                .scheduleNote(schedule.getScheduleNote())
                .scheduleType(schedule.getScheduleType())
                .allDay(schedule.getAllDay())
                .del_yn(schedule.getScheduleDel_YN())
                .MemberId(schedule.getMemberId())
                .build();
    }


}
