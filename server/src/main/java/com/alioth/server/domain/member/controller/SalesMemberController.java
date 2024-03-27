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
        CommonResponse commonResponse = salesMemberService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commonResponse);
    }

    @PatchMapping("/api/members/{id}/password")
    public ResponseEntity<?> updateMemberPassword(@RequestBody @Valid SalesMemberUpdatePassword dto,
                                                  @PathVariable("id")Long id) {
        SalesMembers member = salesMemberService.updatePassword(dto, id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(member.getName());
    }

    @PatchMapping("/api/members/{id}/info")
    public ResponseEntity<?> updateMemberInfo(@PathVariable("id")Long id) {


        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

}
