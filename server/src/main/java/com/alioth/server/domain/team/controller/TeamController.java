package com.alioth.server.domain.team.controller;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.res.SalesMemberTeamListResDto;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamAddMemberDto;
import com.alioth.server.domain.team.dto.TeamCreateDto;
import com.alioth.server.domain.team.dto.TeamDto;
import com.alioth.server.domain.team.dto.TeamUpdateDto;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    private final SalesMemberService salesMemberService;
    private final TypeChange typeChange;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createTeam(@RequestBody TeamCreateDto dto) {
        SalesMembers teamManager = salesMemberService.findBySalesMemberCode(dto.teamManagerCode());
        if (teamManager.getRank() == SalesMemberType.MANAGER) {
            Team team = teamService.createTeam(dto, teamManager);
            salesMemberService.updateTeam(teamManager.getId(), team);
            List<SalesMemberTeamListResDto> list = team.getTeamMembers().stream().map(typeChange::salesMemberToSalesMemberTeamListResDto).toList();
            return CommonResponse.responseMessage(
                    HttpStatus.CREATED,
                    "successfully created",
                    typeChange.teamToTeamDto(team,list)
            );
        } else {
            throw new IllegalArgumentException("직급을 확인해주세요");
        }
    }

    @PatchMapping("/update/{teamId}")
    public ResponseEntity<CommonResponse> updateTeam(@RequestBody @Valid TeamUpdateDto dto,
                                        @PathVariable("teamId") Long id) {
        SalesMembers teamManager = salesMemberService.findBySalesMemberCode(dto.teamManagerCode());
        if (teamManager.getRank() == SalesMemberType.MANAGER) {
            teamService.updateTeam(dto, id);
            return CommonResponse.responseMessage(
                    HttpStatus.CREATED, "successfully updated"
            );
        } else {
            throw new IllegalArgumentException("직급을 확인해주세요");
        }
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<CommonResponse> deleteTeam(@PathVariable("teamId") Long id) {
        teamService.deleteTeam(id);
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "successfully deleted"
        );
    }

    @GetMapping("/detail/{teamId}")
    public ResponseEntity<CommonResponse> teamDetail(@PathVariable("teamId") Long id) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "successfully loaded",
                teamService.findByTeamId(id)
        );
    }

    //팀 상세 정보 조회
    @GetMapping("/info/{teamId}")
    public ResponseEntity<CommonResponse> teamInfo(@PathVariable("teamId") Long teamId ) {

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "successfully loaded",
                teamService.findById(teamId));
    }

    //팀원 추가
    @PostMapping("/addMembers/{teamId}")
    public ResponseEntity<CommonResponse> addMembers(@RequestBody TeamAddMemberDto dto,
                                                     @PathVariable("teamId") Long teamId) {
        List<SalesMembers> teamMembers = new ArrayList<>();
        for(Long salesMemberCode: dto.salesMemberCodes()){
            SalesMembers teamMember=salesMemberService.findBySalesMemberCode(salesMemberCode);
            if(Objects.equals(teamMember.getQuit(), "N")){
                salesMemberService.updateTeam(teamMember.getId(),teamService.findById(teamId));
                teamMembers.add(teamMember);
            }
        }
        teamService.addMembersToTeam(teamId,teamMembers);
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "successfully added"
        );
    }
}
