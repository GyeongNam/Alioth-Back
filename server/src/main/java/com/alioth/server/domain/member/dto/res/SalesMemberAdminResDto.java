package com.alioth.server.domain.member.dto.res;


import com.alioth.server.domain.member.domain.SalesMemberType;
import lombok.Builder;

@Builder
public record SalesMemberAdminResDto(
    String email,
    String phone,
    String name,
    String password,
    String birthDay,
    String address,
    String profileImage,
    String performanceReview,
    Long teamId,
    SalesMemberType rank

) {

}
