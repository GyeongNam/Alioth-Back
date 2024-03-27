package com.alioth.server.domain.team.dto;

import com.alioth.server.domain.member.dto.res.SalesMemberAdminResDto;
import lombok.Builder;

import java.util.List;

@Builder
public record TeamUpdateDto(

    String teamName,
    Long teamManagerCode

){}
