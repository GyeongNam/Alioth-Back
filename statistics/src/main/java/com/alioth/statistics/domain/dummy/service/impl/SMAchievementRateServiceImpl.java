package com.alioth.statistics.domain.dummy.service.impl;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.service.SMAchievementRateService;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.target.sm.domain.SMSalesTarget;
import com.alioth.statistics.domain.target.sm.repository.SMSalesTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SMAchievementRateServiceImpl implements SMAchievementRateService {

    private final ContractRepository contractRepository;
    private final SalesMemberRepository salesMemberRepository;
    private final SMSalesTargetRepository smSalesTargetRepository;

    @Override
    public Map<SalesMembers, String> achievementRatePercent() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();

        Map<SalesMembers, String> result = new LinkedHashMap<>();

        for (var member : memberList) {
            BigInteger contractSum = contractRepository.findBySalesMembers(member).stream()
                    .map(x -> new BigInteger(x.getContractTotalPrice()))
                    .reduce(BigInteger.ZERO, BigInteger::add);

            BigInteger targetSum = smSalesTargetRepository.findBySalesMembers(member).stream()
                    .map(m -> BigInteger.valueOf(m.getTargetPrice()))
                    .reduce(BigInteger.ZERO, BigInteger::add);

            result.put(member, contractSum.divide(targetSum) + "%");
        }

        return result;
    }

    @Override
    public Map<SalesMembers, String> achievementRateCount() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();
        List<Contract> memberByContractList = contractRepository.findBySalesMembers(memberList.get(2));
        List<SMSalesTarget> memberByTargetList = smSalesTargetRepository.findBySalesMembers(memberList.get(2));
        Map<SalesMembers, String> result = new LinkedHashMap<>();

        for (var member : memberList) {

            int size = contractRepository.findBySalesMembers(member).size();
            Long memberTargetCount = smSalesTargetRepository.findBySalesMembers(member).stream()
                    .map(SMSalesTarget::getTargetCount)
                    .reduce(0L, Long::sum);

            double temp = ((double) size / (double)memberTargetCount) * 100;
            String res = String.format("%.3f", temp);
            result.put(member, res + "%");
        }

        return result;
    }
}
