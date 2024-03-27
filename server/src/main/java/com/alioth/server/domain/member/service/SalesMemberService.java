package com.alioth.server.domain.member.service;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdatePerformanceReview;
import com.alioth.server.domain.member.dto.res.SalesMemberAdminResDto;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdatePassword;
import com.alioth.server.domain.member.dto.req.SalesMemberAdminUpdateReqDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesMemberService {

    private final SalesMemberRepository salesMemberRepository;
    private final TeamService teamService;

    @Transactional
    public SalesMembers create(SalesMemberCreateReqDto dto) {
        LocalDateTime date = LocalDateTime.now();
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonthValue());

        SalesMembers member = SalesMembers.builder()
                .salesMemberCode(createSalesMemberCode())
                .email(dto.email())
                .phone(dto.phone())
                .name(dto.name())
                .password(dto.password())
                .birthDay(dto.birthDay())
                .address(dto.address())
                .rank(dto.rank())
                .build();

        return salesMemberRepository.save(member);
    }

    private Long createSalesMemberCode() {
        LocalDateTime date = LocalDateTime.now();
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonthValue());
        Long id;
        SalesMembers findFirstMember = salesMemberRepository.findFirstByOrderByIdDesc();

        if(findFirstMember == null) {
            id = 1L;
        }else {
            id = findFirstMember.getId() + 1;
        }


        Long MemberCode = Long.valueOf(year + month + id.toString());

        return MemberCode;
    }

    @Transactional
    public SalesMembers updatePassword(SalesMemberUpdatePassword dto, Long id) {
        SalesMembers findMember = salesMemberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 계정을 찾을 수 없습니다."));
        findMember.updatePassword(dto.password());
        salesMemberRepository.save(findMember);
        return findMember;
    }


    public SalesMembers findById(Long memberId){
        return salesMemberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
    }

    public SalesMembers findBySalesMemberCode(Long salesMemberCode){
        return salesMemberRepository.findBySalesMemberCode(salesMemberCode);
    }



    //관리자 사원 정보 수정(권한, 팀 소속) : HJ
    @Transactional
    public SalesMemberAdminResDto adminMemberUpdate (Long memberId,SalesMemberAdminUpdateReqDto dto) {
        SalesMembers member = this.findById(memberId);
        Team team = teamService.getTeam(dto.teamCode());
        member.updateAdmin(dto, team);

        return SalesMemberAdminResDto.builder()
                .rank(member.getRank())
                .birthDay(member.getBirthDay())
                .address(member.getAddress())
                .phone(member.getPhone())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
    //관리자 고과정보 수정
    @Transactional
    public void adminMemberPr (Long memberId, SalesMemberUpdatePerformanceReview dto){
        SalesMembers member = this.findById(memberId);
        member.updatePr(dto);
        salesMemberRepository.save(member);
    }

    //관리자 사원 정보 조회
    @Transactional
    public SalesMemberAdminResDto memberDetail(Long memberId) {
        SalesMembers salesMembers = salesMemberRepository.findById(memberId).orElseThrow(()->
                                                            new EntityNotFoundException("존재하지 않는 사원입니다."));
        return SalesMemberAdminResDto.builder()
                .rank(salesMembers.getRank())
                .birthDay(salesMembers.getBirthDay())
                .address(salesMembers.getAddress())
                .phone(salesMembers.getPhone())
                .name(salesMembers.getName())
                .email(salesMembers.getEmail())
                .build();
    }

    //사원 리스트
    public List<SalesMemberAdminResDto> findAllByTeamId(Long teamId){
        List<SalesMembers> memberList= salesMemberRepository.findAllByTeamId(teamId);
        List<SalesMemberAdminResDto> list = new ArrayList<>();
        for(SalesMembers sm: memberList){
            SalesMemberAdminResDto dto= SalesMemberAdminResDto.builder()
                    .name(sm.getName())
                    .rank(sm.getRank())
                    .profileImage(sm.getProfileImage())
                    .build();
            list.add(dto);
        }
        return list;
    }

    public void updateTeam(Long memberId,Team team){
        SalesMembers member= this.findById(memberId);
        member.updateTeam(team);
        salesMemberRepository.save(member);
    }


}
