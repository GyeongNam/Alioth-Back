package com.alioth.statistics.domain.batch.dto.res;

import lombok.Builder;

@Builder
public record BatchTeamSalesResDto(
    String teamName,
    String teamCode,
    String contractPrice,
    String contractCount,
    String cancelPrice,
    String cancelCount
) {
}
