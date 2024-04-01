package com.alioth.server.domain.answer.dto.res;

import lombok.Builder;

@Builder
public record AnswerResDto(
        String title,
        String content,
        String answer_name
) {}
