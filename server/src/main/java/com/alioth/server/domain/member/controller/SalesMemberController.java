package com.alioth.server.domain.member.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdatePassword;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SalesMemberController {

    private final SalesMemberService salesMemberService;

    @PostMapping("/api/members")
    public ResponseEntity<?> createMember(@RequestBody @Valid SalesMemberCreateReqDto dto) {
        SalesMembers member = salesMemberService.create(dto);

        CommonResponse commonResponse = CommonResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message(member.getName() + "님의 회원가입이 완료되었습니다.")
                .result(member.getSalesMemberCode())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commonResponse);
    }

    @PatchMapping("/api/members/{id}/password")
    public ResponseEntity<?> updateMemberPassword(@RequestBody @Valid SalesMemberUpdatePassword dto,
                                                  @PathVariable("id")Long id) {
        SalesMembers member = salesMemberService.updatePassword(dto, id);

        CommonResponse commonResponse = CommonResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(member.getName() + "님 비밀번호 변경되었습니다.")
                .result(member.getSalesMemberCode())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(commonResponse);
    }

    @PatchMapping("/api/members/{id}/info")
    public ResponseEntity<?> updateMemberInfo(@PathVariable("id")Long id) {


        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

}
