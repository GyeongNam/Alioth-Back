package com.alioth.statistics.domain.batch.dto.res;

import lombok.Builder;

@Builder
public record BatchMemberSalesResDto(
    String salesMemberName,
    String salesMemberCode,
    String contractPrice,
    String contractCount,
    String cancelPrice,
    String cancelCount
) {
}
