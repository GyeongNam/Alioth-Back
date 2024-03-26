package com.alioth.server.domain.dummy.service;

import com.alioth.server.domain.dummy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DummyService {

    private final RenewalRepository renewalRepository;
    private final ContractRepository contractRepository;
    private final ContractMembersRepository contractMembersRepository;
    private final InsuranceProductRepository insuranceProductRepository;
    private final CustomRepository customRepository;

    @Autowired
    public DummyService(
            RenewalRepository renewalRepository,
            ContractRepository contractRepository,
            ContractMembersRepository contractMembersRepository,
            InsuranceProductRepository insuranceProductRepository,
            CustomRepository customRepository
    ) {
        this.renewalRepository = renewalRepository;
        this.contractRepository = contractRepository;
        this.contractMembersRepository = contractMembersRepository;
        this.insuranceProductRepository = insuranceProductRepository;
        this.customRepository = customRepository;
    }
}
