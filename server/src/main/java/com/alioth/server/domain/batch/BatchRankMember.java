package com.alioth.server.domain.batch;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchRankMember {

    /* 월 단위 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long memberCode; // 사원 코드

    private Long memberRank; // 사원 랭킹

    private LocalDateTime createdDate; // 날짜

    private String contractPrice; // 계약 총금액

    private String contractCount; // 계약 건수

}
