package com.alioth.server.domain.member.controller;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberReqDto;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class SalesMemberController {

    private final SalesMemberService salesMemberService;

    @PostMapping("/api/members")
    public ResponseEntity<?> createMember(@RequestBody @Valid SalesMemberReqDto dto) {
        SalesMembers members = salesMemberService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(members.getName()+ "님 회원가입이 되었습니다.");
    }

    @PatchMapping("/api/members/{id}/password")
    public ResponseEntity<?> updateMemberPassword(@RequestBody @Valid SalesMemberReqDto dto,
                                                  @PathVariable("id")Long id) {


        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @PatchMapping("/api/members/{id}/info")
    public ResponseEntity<?> updateMemberInfo(@PathVariable("id")Long id) {


        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

}
