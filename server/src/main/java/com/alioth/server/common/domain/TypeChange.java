package com.alioth.server.common.domain;

import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.dto.res.SMTeamListResDto;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.dto.req.ScheduleReqDto;
import com.alioth.server.domain.schedule.dto.res.ScheduleResDto;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamResDto;
import com.alioth.server.domain.team.dto.TeamReqDto;
import org.springframework.stereotype.Component;

import java.util.List;

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


    public SMTeamListResDto smToSmTeamListResDto(SalesMembers member){
        return SMTeamListResDto.builder()
                .rank(member.getRank())
                .name(member.getName())
                .profileImage(member.getProfileImage())
                .salesMemberCode(member.getSalesMemberCode())
                .phone(member.getPhone())
                .email(member.getEmail())
                .build();
    }

    public SalesMemberResDto smToSmResDto(SalesMembers member){
        return SalesMemberResDto.builder()
                .rank(member.getRank())
                .salesMemberCode(member.getSalesMemberCode())
                .birthDay(member.getBirthDay())
                .performanceReview(member.getPerformanceReview())
                .teamCode(member.getTeam().getTeamCode())
                .teamName(member.getTeam().getTeamName())
                .address(member.getAddress())
                .officeAddress(member.getOfficeAddress())
                .extensionNumber(member.getExtensionNumber())
                .phone(member.getPhone())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    public TeamResDto teamToTeamReqDto(Team team, List<SMTeamListResDto> list){
        return TeamResDto.builder()
                .teamCode(team.getTeamCode())
                .teamName(team.getTeamName())
                .teamManagerCode(team.getTeamManagerCode())
                .teamMemberList(list)
                .build();
    }

    public Team teamCreateDtoToTeam(TeamReqDto dto, String teamCode){
        return Team.builder()
                .teamCode(teamCode)
                .teamName(dto.teamName())
                .teamManagerCode(dto.teamManagerCode())
                .build();
    }







}
