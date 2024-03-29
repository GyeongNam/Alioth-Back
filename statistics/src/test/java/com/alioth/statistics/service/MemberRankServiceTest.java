package com.alioth.statistics.service;

import com.alioth.statistics.domain.dummy.repository.ContractMembersRepository;
import com.alioth.statistics.domain.dummy.repository.InsuranceProductRepository;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRankServiceTest {

    @Autowired private StatisticsRankService memberRankService;
    @Autowired private InsuranceProductRepository insuranceProductRepository;
    @Autowired private SalesMemberRepository salesMemberRepository;
    @Autowired private ContractMembersRepository contractMembersRepository;


    @Test
    @DisplayName("맴버 사원수 확인 테스트")
    @Transactional
    public void 맴버사원수확인테스트() {
        Long rank = memberRankService.rank();
        long count1 = insuranceProductRepository.count();
        long count2 = salesMemberRepository.count();
        long count3 = contractMembersRepository.count();

        Assertions.assertThat(rank).isEqualTo(100000L);
        Assertions.assertThat(count1).isEqualTo(2000L);
        Assertions.assertThat(count2).isEqualTo(3L);
        Assertions.assertThat(count3).isEqualTo(3000L);
    }

}