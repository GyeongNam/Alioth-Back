package com.alioth.server.common.domain;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.dto.req.ScheduleCreateDto;
import com.alioth.server.domain.schedule.dto.res.ScheduleResDto;
import org.springframework.stereotype.Component;

@Component
public class TypeChange {

    public Schedule ScheduleCreateDtoToSchedule(ScheduleCreateDto scheduleCreateDto, SalesMembers salesMembers){
        return Schedule.builder()
                .scheduleStartTime(scheduleCreateDto.scheduleStartTime())
                .scheduleEndTime(scheduleCreateDto.scheduleEndTime())
                .scheduleNote(scheduleCreateDto.scheduleNote())
                .scheduleType(scheduleCreateDto.scheduleType())
                .allDay(scheduleCreateDto.allDay())
                .salesMembers(salesMembers) // 사원
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
                .MemberId(schedule.getSalesMembers().getId())
                .build();
    }


}
