package com.alioth.server.domain.member.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.common.jwt.JwtTokenProvider;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdatePassword;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SalesMemberService {

    private final PasswordEncoder passwordEncoder;
    private final SalesMemberRepository salesMemberRepository;
    private final TypeChange typeChange;

    @Transactional
    public CommonResponse create(SalesMemberCreateReqDto dto) {
        LocalDateTime date = LocalDateTime.now();
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonthValue());
        Long salesMemberCode = createSalesMemberCode();
        String encodePassword = passwordEncoder.encode(dto.password());
        SalesMembers createMember = typeChange.salesMemberCreateReqDtoToSalesMembers(dto, salesMemberCode, encodePassword);

        CommonResponse commonResponse = CommonResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message(createMember.getName() + "님의 회원가입이 완료되었습니다.")
                .result(createMember.getSalesMemberCode())
                .build();

        salesMemberRepository.save(createMember);

        return commonResponse;
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
        //salesMemberRepository.save(findMember);
        return findMember;
    }
}
