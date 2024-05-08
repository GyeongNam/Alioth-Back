package com.alioth.server.domain.answer.dto.res;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnswerResDto(
        Long answerId,
        String title,
        String content,
        String answer_name,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {}
