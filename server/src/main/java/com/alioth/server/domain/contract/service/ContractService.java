package com.alioth.server.domain.contract.service;

import com.alioth.server.domain.contract.domain.Contract;
import com.alioth.server.domain.contract.dto.req.ContractCreateDto;
import com.alioth.server.domain.contract.dto.req.ContractUpdateDto;
import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.repository.ContractRepository;
import com.alioth.server.domain.dummy.domain.ContractMembers;
import com.alioth.server.domain.dummy.domain.Custom;
import com.alioth.server.domain.dummy.domain.InsuranceProduct;
import com.alioth.server.domain.dummy.service.DummyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final DummyService dummyService;

    public ContractResDto createContract(ContractCreateDto dto) {
        ContractMembers contractMembers = dummyService.contractManagerFindById(dto.contractMemberId());
        Custom custom = dummyService.customFindById(dto.customId());
        InsuranceProduct insuranceProduct = dummyService.insuranceProductFindById(dto.insuranceProductId());
        Contract contract = Contract.builder()
                .contractCode(dto.contractCode())
                .contractDate(dto.contractDate())
                .contractExpireDate(dto.contractExpireDate())
                .contractPeriod(dto.contractPeriod())
                .contractTotalPrice(dto.contractTotalPrice())
                .contractPaymentAmount(dto.contractPaymentAmount())
                .contractPaymentFrequency(dto.contractPaymentFrequency())
                .contractPaymentMaturityInstallment(dto.contractPaymentMaturityInstallment())
                .contractCount(dto.contractCount())
                .contractPaymentMethod(dto.contractPaymentMethod())
                .contractPayer(dto.contractPayer())
                .contractConsultation(dto.contractConsultation())
                .contractStatus(dto.contractStatus())
                .contractMembers(contractMembers)
                .custom(custom)
                .insuranceProduct(insuranceProduct)
                .build();
        contract = contractRepository.save(contract);
        return toContractResDto(contract);
    }
    @Transactional
    public ContractResDto updateContract(Long contractId, ContractUpdateDto dto) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));
        contract.update(dto);
        contract = contractRepository.save(contract);
        return toContractResDto(contract);
    }

    public void deleteContract(Long contractId) {
        if (!contractRepository.existsById(contractId)) {
            throw new EntityNotFoundException("계약을 찾을 수 없습니다.");
        }
        contractRepository.deleteById(contractId);
    }
    public List<ContractResDto> listAllContracts() {
        return contractRepository.findAll().stream()
                .map(this::toContractResDto)
                .collect(Collectors.toList());
    }
    private ContractResDto toContractResDto(Contract contract) {
        return ContractResDto.builder()
                .contractId(contract.getContractId())
                .contractCode(contract.getContractCode())
                .contractDate(contract.getContractDate())
                .contractExpireDate(contract.getContractExpireDate())
                .contractPeriod(contract.getContractPeriod())
                .contractTotalPrice(contract.getContractTotalPrice())
                .contractPaymentAmount(contract.getContractPaymentAmount())
                .contractPaymentFrequency(contract.getContractPaymentFrequency())
                .contractPaymentMaturityInstallment(contract.getContractPaymentMaturityInstallment())
                .contractCount(contract.getContractCount())
                .contractPaymentMethod(contract.getContractPaymentMethod())
                .contractPayer(contract.getContractPayer())
                .contractConsultation(contract.getContractConsultation())
                .contractStatus(contract.getContractStatus())
                .insuranceProductName(contract.getInsuranceProduct() != null ? contract.getInsuranceProduct().getInsuranceName() : null)
                .customName(contract.getCustom() != null ? contract.getCustom().getCustomerName() : null)
                .contractMemberName(contract.getContractMembers() != null ? contract.getContractMembers().getCM_name() : null)
                .build();
    }
}
