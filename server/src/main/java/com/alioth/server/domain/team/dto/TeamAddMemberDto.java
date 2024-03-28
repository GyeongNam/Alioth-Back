package com.alioth.server.domain.team.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TeamAddMemberDto(

  List<Long> salesMemberCodes

) {}