package com.alioth.server.domain.contract.domain;

import com.alioth.server.common.domain.BaseEntity;
import com.alioth.server.domain.contract.dto.req.ContractUpdateDto;
import com.alioth.server.domain.dummy.domain.*;
import com.alioth.server.domain.member.domain.SalesMembers;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @Enumerated(EnumType.STRING)
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
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @ManyToOne
    @JoinColumn(name = "insurance_id")
    @Enumerated(EnumType.STRING)
    private InsuranceProduct insuranceProduct;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Custom custom;

    @ManyToOne
    @JoinColumn(name = "CM_id")
    @Enumerated(EnumType.STRING)
    private ContractMembers contractMembers;

    @ManyToOne
    @JoinColumn(name = "SM_id")
    private SalesMembers salesMembers;

    @Column(nullable = true)
    private String cancellationReason;

    public void cancel(String reason) {
        this.contractStatus = ContractStatus.Cancellation;
        this.cancellationReason = reason;  // 해지 사유를 저장
    }


    public void update(ContractUpdateDto dto) {
        if (dto.contractPeriod() != null) {
            this.contractPeriod = dto.contractPeriod();
        }
        if (dto.contractPaymentFrequency() != null) {
            this.contractPaymentFrequency = dto.contractPaymentFrequency();
        }
        if (dto.contractPayer() != null) {
            this.contractPayer = dto.contractPayer();
        }
        if (dto.contractPaymentMethod() != null) {
            this.contractPaymentMethod = dto.contractPaymentMethod();
        }
        if (dto.contractConsultation() != null) {
            this.contractConsultation = dto.contractConsultation();
        }
        if (dto.contractConsultation() != null) {
        this.contractStatus = ContractStatus.valueOf(dto.contractStatus());
        }
    }
    public void cancel() {
        this.contractStatus = ContractStatus.Cancellation;
    }

}
