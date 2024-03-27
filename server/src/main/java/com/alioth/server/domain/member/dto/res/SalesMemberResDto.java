package com.alioth.server.domain.member.dto.res;


import com.alioth.server.domain.member.domain.SalesMemberType;
import lombok.Builder;
import lombok.Getter;

@Builder
public record SalesMemberResDto(
        String email,
        String phone,
        String name,
        String password,
        String birthDay,
        String address,
        String profileImage,
        String performanceReview,
        SalesMemberType rank
) {


}
