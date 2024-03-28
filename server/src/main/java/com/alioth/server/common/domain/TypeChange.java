package com.alioth.server.common.domain;

import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.dto.req.ScheduleReqDto;
import com.alioth.server.domain.schedule.dto.res.ScheduleResDto;
import org.springframework.stereotype.Component;

@Component
public class TypeChange {

    public Schedule ScheduleCreateDtoToSchedule(ScheduleReqDto scheduleReqDto, SalesMembers salesMembers){
        return Schedule.builder()
                .scheduleStartTime(scheduleReqDto.scheduleStartTime())
                .scheduleEndTime(scheduleReqDto.scheduleEndTime())
                .scheduleNote(scheduleReqDto.scheduleNote())
                .scheduleType(scheduleReqDto.scheduleType())
                .allDay(scheduleReqDto.allDay())
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


    public SalesMembers salesMemberCreateReqDtoToSalesMembers(SalesMemberCreateReqDto dto, Long salesMemberCode, String encodePassword) {
        SalesMembers member = SalesMembers.builder()
                .salesMemberCode(salesMemberCode)
                .email(dto.email())
                .phone(dto.phone())
                .name(dto.name())
                .password(encodePassword)
                .birthDay(dto.birthDay())
                .address(dto.address())
                .rank(dto.rank())
                .build();

        return member;
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

    public LoginResDto memberToLoginResDto(SalesMembers findMember, String accessToken, String refreshToken) {
        return LoginResDto.builder()
                .memberCode(findMember.getSalesMemberCode())
                .name(findMember.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
