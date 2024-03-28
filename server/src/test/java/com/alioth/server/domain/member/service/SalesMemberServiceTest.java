package com.alioth.server.domain.member.service;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.*;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
class SalesMemberServiceTest {


    @Autowired
    private SalesMemberService salesMemberService;

    @Autowired
    private SalesMemberRepository salesMemberRepository;

    @Test
    @DisplayName("맴버등록하기")
    public void 맴버등록테스트() {
        SalesMemberCreateReqDto dto = SalesMemberCreateReqDto.builder()
                .email("sj@naver.com") // 마스킹
                .phone("010-1234-1234") // 끝 4자리 마스킹
                .name("손흥민")
                .password("a1234567")
                .birthDay("990123") // 마스킹
                .address("축신") // 마스킹
                .rank(SalesMemberType.FP)
                .build();

        SalesMembers createMember = salesMemberService.create(dto);
        SalesMembers findMember = salesMemberRepository.findById(createMember.getId()).orElse(null);

        Assertions.assertThat(createMember.getSalesMemberCode()).isEqualTo(findMember.getSalesMemberCode());
    }

    @Test
    @DisplayName("비밀번호변경 테스트")
    public void 비밀번호변경확인() {
        SalesMemberUpdatePassword passDto = SalesMemberUpdatePassword.builder().password("12389asdj@1!@").build();
        Long id = 7L;

        SalesMembers members = salesMemberService.updatePassword(passDto, id);
        salesMemberRepository.save(members);
        Assertions.assertThat(passDto.password()).isEqualTo(members.getPassword());
    }

    @Test
    @DisplayName("관리자 사원 정보(소속 변경, 직급) 수정")
    public void adminMemberInfoUpdateTest(){
        SalesMemberAdminUpdateReqDto dto = SalesMemberAdminUpdateReqDto.builder()
                .teamCode("SALES005")
                .rank(SalesMemberType.FP)
                .build();
        Long id = 6L;

        SalesMemberResDto member = salesMemberService.adminMemberUpdate(id,dto);
        Assertions.assertThat(dto.rank()).isEqualTo(member.rank());
    }


    @Test
    @DisplayName("고과평가")
    public void setPrTest(){
        SalesMemberUpdatePerformanceReview dto = SalesMemberUpdatePerformanceReview.builder()
                .performanceReview("B")
                .build();
        Long id = 4L;

        salesMemberService.adminMemberPr(id,dto);
        SalesMembers member = salesMemberService.findById(id);

        Assertions.assertThat(dto.performanceReview()).isEqualTo(member.getPerformanceReview());
    }

    @Test
    @DisplayName("사원 정보 조회")
    public void getMemberDetailTest(){
        Long id = 4L;
        SalesMemberResDto member = salesMemberService.memberDetail(id);
        assertEquals(202434,member.salesMemberCode());
    }

    @Test
    @DisplayName("내 정보 수정")
    public void updateMyInfoTest(){
        Long id = 4L;
        SalesMemberUpdateReqDto dto = SalesMemberUpdateReqDto.builder()
                .birthDay("1998-12-03")
                .phone("010-8556-4451")
                .name("카리나")
                .email("asepa@gmail.com")
                .address("서울특별시 압구정구")
                .extensionNumber("02-6642-8789")
                .officeAddress("10층 1002호")
                .profileImage(null)
                .build();

        SalesMemberResDto member = salesMemberService.updateMyInfo(id,dto);
        assertEquals("1998-12-03",member.birthDay());
    }
}