package com.alioth.server.domain.member.service;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdatePassword;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SalesMemberService {

    private final SalesMemberRepository salesMemberRepository;

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
        }
        id = findFirstMember.getId() + 1;

        Long MemberCode = Long.valueOf(year + month + id.toString());

        return MemberCode;
    }

    @Transactional
    public SalesMembers updatePassword(SalesMemberUpdatePassword dto, Long id) {
        SalesMembers findMember = salesMemberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 계정을 찾을 수 없습니다."));
        findMember.updatePassword(dto.password());
        //salesMemberRepository.save(findMember);
        return findMember;
    }
}
