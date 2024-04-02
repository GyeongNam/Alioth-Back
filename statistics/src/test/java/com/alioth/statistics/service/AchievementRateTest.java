package com.alioth.statistics.service;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.target.sm.domain.SMSalesTarget;
import com.alioth.statistics.domain.target.sm.repository.SMSalesTargetRepository;
import com.alioth.statistics.domain.target.team.domain.TeamTarget;
import com.alioth.statistics.domain.target.team.repository.TeamTargetRepository;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
class AchievementRateTest {

    @Autowired private ContractRepository contractRepository;

    @Autowired private SalesMemberRepository salesMemberRepository;
    @Autowired private SMSalesTargetRepository smSalesTargetRepository;

    @Autowired private TeamTargetRepository teamTargetRepository;
    @Autowired private TeamRepository teamRepository;


    @Test
    @DisplayName("개인 달성율")
    public void 개인달성율() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();
//        List<Contract> memberByContractList = contractRepository.findBySalesMembers(memberList.get(2));
//        List<SMSalesTarget> memberByTargetList = smSalesTargetRepository.findBySalesMembers(memberList.get(2));

//        System.out.println("맴버들 : " + memberList);
//        System.out.println("계약목록 = " + memberByContractList);
//        System.out.println("개인목표 = " + memberByTargetList);

        Map<SalesMembers, String> result = new LinkedHashMap<>();

        for (var member : memberList) {
            BigInteger contractSum = contractRepository.findBySalesMembers(member).stream()
                    .map(x -> new BigInteger(x.getContractTotalPrice()))
                    .reduce(BigInteger.ZERO, BigInteger::add);

            BigInteger targetSum = smSalesTargetRepository.findBySalesMembers(member).stream()
                    .map(m -> BigInteger.valueOf(m.getTargetPrice()))
                    .reduce(BigInteger.ZERO, BigInteger::add);

//            System.out.println("contractSum = " + contractSum);
//            System.out.println("targetSum = " + targetSum);

            result.put(member, contractSum.divide(targetSum) + "%");
        }

        System.out.println("개인 목표 퍼센트" + result);

    }

    @Test
    @DisplayName("개인 달성건수")
    public void 개인달성건() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();
        List<Contract> memberByContractList = contractRepository.findBySalesMembers(memberList.get(2));
        List<SMSalesTarget> memberByTargetList = smSalesTargetRepository.findBySalesMembers(memberList.get(2));
        Map<SalesMembers, String> result = new LinkedHashMap<>();

        System.out.println("맴버들 : " + memberList);
        System.out.println("계약목록 = " + memberByContractList);
        System.out.println("개인목표 = " + memberByTargetList);

        for (var member : memberList) {

            int size = contractRepository.findBySalesMembers(member).size();
            Long memberTargetCount = smSalesTargetRepository.findBySalesMembers(member).stream()
                    .map(SMSalesTarget::getTargetCount)
                    .reduce(0L, Long::sum);

            double temp = ((double) size / (double)memberTargetCount) * 100;
            String res = String.format("%.3f", temp);
            result.put(member, res + "%");
//            System.out.println(member.getName() + " = " + size + " - " + memberTargetCount);
//            System.out.println("퍼센트 = " + res);

        }

        System.out.println("result = " + result);

    }






}
