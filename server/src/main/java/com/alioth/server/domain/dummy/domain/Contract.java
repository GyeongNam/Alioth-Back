package com.alioth.server.domain.dummy.domain;

import com.alioth.server.common.domain.BaseEntity;
import com.alioth.server.domain.member.domain.SalesMembers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;
    @Column(nullable = false)
    private String contractCode;
    @Column(nullable = false)
    private LocalDateTime contractDate;
    @Column(nullable = false)
    private LocalDateTime contractExpireDate;
    @Column(nullable = false)
    private String contractPeriod;
    @Column(nullable = false)
    private String contractTotalPrice;
    @Column(nullable = false)
    private String contractPaymentAmount;
    @Column(nullable = false)
    private PaymentFrequency contractPaymentFrequency;
    @Column(nullable = false)
    private Long contractPaymentMaturityInstallment;
    @Column(nullable = false)
    private Long contractCount;
    @Column(nullable = false)
    private String contractPaymentMethod;
    @Column(nullable = false)
    private String contractPayer;
    @Column(nullable = false)
    private String contractConsultation;
    @Column(nullable = false)
    private ContractStatus contractStatus;

    @ManyToOne
    @JoinColumn(name = "insurance_id")
    private InsuranceProduct insuranceProduct;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Custom custom;

    @ManyToOne
    @JoinColumn(name = "CM_id")
    private ContractMembers contractMembers;

    @ManyToOne
    @JoinColumn(name = "SM_id")
    private SalesMembers salesMembers;
}
