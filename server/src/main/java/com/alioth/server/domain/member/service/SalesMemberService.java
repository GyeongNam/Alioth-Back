package com.alioth.server.domain.member.service;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberReqDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalesMemberService {

    private final SalesMemberRepository salesMemberRepository;

    public SalesMembers create(SalesMemberReqDto dto) {
        String uuid = UUID.randomUUID().toString();


        LocalDateTime date = LocalDateTime.now();
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonthValue());


        SalesMembers member = SalesMembers.builder()
                .salesMemberCode(4321L)
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



    public Long createSalesMemberCode(String year, String monthValue) {


        return 2L;
    }

}
