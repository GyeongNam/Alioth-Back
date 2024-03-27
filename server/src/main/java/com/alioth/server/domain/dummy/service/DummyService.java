package com.alioth.server.domain.dummy.service;

import com.alioth.server.domain.contract.repository.RenewalRepository;
import com.alioth.server.domain.dummy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DummyService {

    private final RenewalRepository renewalRepository;
    private final ContractMembersRepository contractMembersRepository;
    private final InsuranceProductRepository insuranceProductRepository;
    private final CustomRepository customRepository;
}
