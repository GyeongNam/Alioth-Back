package com.alioth.server.domain.member.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdatePassword;
import com.alioth.server.domain.member.dto.req.SalesMemberAdminUpdateReqDto;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdatePerformanceReview;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class SalesMemberController {

    private final SalesMemberService salesMemberService;

    @PostMapping("/create")
    public ResponseEntity<?> createMember(@RequestBody @Valid SalesMemberCreateReqDto dto) {
        SalesMembers members = salesMemberService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(members.getName()+ "님 회원가입이 되었습니다.");
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updateMemberPassword(@RequestBody @Valid SalesMemberUpdatePassword dto,
                                                  @PathVariable("id")Long id) {
        SalesMembers member = salesMemberService.updatePassword(dto, id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(member.getName());
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<?> updateMemberInfo(@PathVariable("id")Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }


    //HJ
    //관리자 사원 부서, 권한 수정
    @PatchMapping("/admin/update/{memberId}")
    public ResponseEntity<CommonResponse> updateMemberAdminInfo(@PathVariable("memberId")Long memberId,
                                                                @RequestBody @Valid SalesMemberAdminUpdateReqDto dto) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "successfully updated",
                salesMemberService.adminMemberUpdate(memberId,dto)
        );
    }

    //고과평가
    @PatchMapping("/admin/pr/{id}")
    public ResponseEntity<CommonResponse> updateMemberAdminPrInfo(@PathVariable("id")Long id,
                                                     @RequestBody @Valid SalesMemberUpdatePerformanceReview dto) {
        salesMemberService.adminMemberPr(id,dto);
        return CommonResponse.responseMessage(
                        HttpStatus.OK,
                "success"
                );
    }

    //사원 상세 조회 (본인, 지점장, 지역장)
    @GetMapping("/details/{memberId}")
    public ResponseEntity<CommonResponse> getMemberInfoAdmin(@PathVariable("memberId") Long memberId ) {

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "success",
                salesMemberService.memberDetail(memberId)
        );
    }
}
