package com.alioth.server.domain.team.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdateReqDto;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.dto.res.SalesMemberTeamListResDto;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamCreateDto;
import com.alioth.server.domain.team.dto.TeamUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TeamServiceTest {

    @Autowired
    private TypeChange typeChange;


    @Autowired
    private TeamService teamService;

    @Autowired
    private SalesMemberService salesMemberService;

    @Test
    @DisplayName("팀 생성")
    public void createTeamTest(){
        String teamCode = teamService.createTeamCode();
        Long teamManagerCode = 202435L;
        SalesMembers teamManager = salesMemberService.findBySalesMemberCode(teamManagerCode);
        if (teamManager.getRank() == SalesMemberType.MANAGER) {
            TeamCreateDto dto = TeamCreateDto.builder()
                    .teamName(teamCode)
                    .teamManagerCode(teamManagerCode)
                    .build();
            Team team = teamService.createTeam(dto,teamManager);
            Assertions.assertEquals(teamCode,team.getTeamCode());
        } else {
            throw new IllegalArgumentException("직급을 확인해주세요");
        }
    }

    @Test
    @DisplayName("팀 삭제")
    public void deleteTeamTest(){
        Long teamId = 2L;
        Team team = teamService.findById(teamId);
        team.deleteTeam();
        assertEquals("Y", team.getDelYN());
    }

    @Test
    @DisplayName("팀 정보 수정")
    public void updateTeamTest(){
        Long teamId = 3L;
        TeamUpdateDto dto = TeamUpdateDto.builder()
                .teamName("SALES102")
                .teamManagerCode(202435L)
                .build();
        Team team = teamService.findById(teamId);
        SalesMembers teamManager = salesMemberService.findBySalesMemberCode(dto.teamManagerCode());
        if(teamManager.getRank()==SalesMemberType.MANAGER){
            teamService.updateTeam(dto,teamId);
            assertEquals("SALES102", team.getTeamName());
            assertEquals(dto.teamManagerCode(), team.getTeamManagerCode());
        }
    }
    @Test
    @DisplayName("팀 상세정보 조회")
    public void getTeamDetailTest(){
        Long teamId = 2L;
        Team team = teamService.findById(teamId);
        team.deleteTeam();
        assertEquals("Y", team.getDelYN());
    }
}

