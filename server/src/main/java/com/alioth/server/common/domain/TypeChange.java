package com.alioth.server.common.domain;

import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
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

    public ScheduleResDto ScheduleToScheduleResDto(Schedule schedule){
        return ScheduleResDto.builder()
                .scheduleId(schedule.getScheduleId())
                .scheduleStartTime(schedule.getScheduleStartTime())
                .scheduleEndTime(schedule.getScheduleEndTime())
                .scheduleNote(schedule.getScheduleNote())
                .scheduleType(schedule.getScheduleType())
                .allDay(schedule.getAllDay())
                .del_yn(schedule.getScheduleDel_YN())
                .memberId(schedule.getSalesMembers().getId())
                .build();
    }

    public BoardResDto BoardToBoardResDto(Board board){
        return BoardResDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .boardType(board.getBoardType())
                .memberId(board.getSalesMembers().getId())
                .build();
    }
    public Board BoardCreateDtoToBoard(BoardCreateDto boardCreateDto, SalesMembers salesMembers){
        return Board.builder()
                .title(boardCreateDto.title())
                .content(boardCreateDto.content())
                .boardType(boardCreateDto.boardType())
                .salesMembers(salesMembers)
                .build();
    }

}
