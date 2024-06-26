package com.alioth.statistics.service.cencellation.rank;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class TeamCancellationServiceTest {

    @Autowired private ContractRepository contractRepository;
    @Autowired private TeamRepository teamRepository;
    private ContractStatus contractStatus = ContractStatus.Cancellation;



    @DisplayName("팀 해약률")
    @Transactional
    public void 팀해약률() {

        Map<Team, String> result = new LinkedHashMap<>();
        List<Team> teamList = teamRepository.findAll();

        for (var team : teamList) {

            List<SalesMembers> teamMembers = team.getTeamMembers();
            BigDecimal teamTotalPrice = BigDecimal.ZERO;
            BigDecimal teamCancelPrice = BigDecimal.ZERO;

            for (var member : teamMembers) {

                List<Contract> contractList = contractRepository.findBySalesMembers(member);
                BigDecimal totalPrice = contractList.stream()
                        .map(x -> new BigDecimal(x.getContractTotalPrice()))
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ONE);

                BigDecimal cancelPrice = contractList.stream()
                        .filter(x -> x.getContractStatus() == ContractStatus.Cancellation)
                        .map(x -> new BigDecimal(x.getContractTotalPrice()))
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ONE);

                teamTotalPrice = teamTotalPrice.add(totalPrice);
                teamCancelPrice = teamCancelPrice.add(cancelPrice);
            }

            BigDecimal percent = new BigDecimal("100");
            BigDecimal divide = teamCancelPrice.divide(teamTotalPrice, 3, RoundingMode.HALF_EVEN);
            BigDecimal multiply = divide.multiply(percent);

            result.put(team, multiply + "%");
        }

        System.out.println("result = " + result);
    }


    @Test
    @DisplayName("팀 해약건")
    @Transactional
    public void 팀해약건() {

        Map<Team, String> result = new LinkedHashMap<>();
        List<Team> teamList = teamRepository.findAll();

        for (var team : teamList) {

            List<SalesMembers> teamMembers = team.getTeamMembers();
            long teamTotalCount = 0L;
            long teamCancelCount = 0L;

            for (var member : teamMembers) {

                List<Contract> contractList = contractRepository.findBySalesMembers(member);
                long totalCount = contractList.stream().count();

                long cancelCount = contractList.stream()
                        .filter(x -> x.getContractStatus() == ContractStatus.Cancellation)
                        .count();

                teamTotalCount += totalCount;
                teamCancelCount += cancelCount;
            }

            double v = ((double)teamCancelCount / (double)teamTotalCount) * 100;
            String strResult = String.format("%.3f", v);
            result.put(team, strResult + "%");
        }

        System.out.println("result = " + result);
    }

}