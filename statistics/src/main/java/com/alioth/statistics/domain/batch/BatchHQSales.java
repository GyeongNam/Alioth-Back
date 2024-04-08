package com.alioth.statistics.domain.batch;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchHQSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String totalPrice;

    @Column(nullable = false)
    private Long totalCount;

    @Column(nullable = false)
    private String cancelPrice;

    @Column(nullable = false)
    private String cancelCount;



}
