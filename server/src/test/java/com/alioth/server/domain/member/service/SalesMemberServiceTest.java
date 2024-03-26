package com.alioth.server.domain.member.service;

import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberReqDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class SalesMemberServiceTest {


    @Autowired
    private SalesMemberService salesMemberService;

    @Autowired
    private SalesMemberRepository salesMemberRepository;

    @Test
    @DisplayName("맴버등록하기")
    public void 맴버등록테스트() {
        SalesMemberReqDto dto = SalesMemberReqDto.builder()
                .email("sj@naver.com") // 마스킹
                .phone("010-1234-1234") // 끝 4자리 마스킹
                .name("준스틴")
                .password("djaksdqw12@")
                .birthDay("990123") // 마스킹
                .address("서울특별시 용산에사는 대통령") // 마스킹
                .rank(SalesMemberType.FP)
                .build();

        SalesMembers createMember = salesMemberService.create(dto);
        SalesMembers findMember = salesMemberRepository.findById(createMember.getId()).orElse(null);

        Assertions.assertThat(createMember.getSalesMemberCode()).isEqualTo(findMember.getSalesMemberCode());
    }




}