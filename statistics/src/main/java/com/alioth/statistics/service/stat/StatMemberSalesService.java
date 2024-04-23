package com.alioth.statistics.service.stat;

import com.alioth.statistics.domain.batch.dto.res.BatchMemberSalesResDto;
import com.alioth.statistics.domain.batch.repository.BatchMemberSalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StatMemberSalesService {

    private final BatchMemberSalesRepository memberSalesRepository;

    public List<BatchMemberSalesResDto> getMemberSales() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear(); int month = now.getMonthValue(); int day = now.getDayOfMonth(); int hour = 0; int minute = 0;
        LocalDateTime endTime = LocalDateTime.of(year, month, 10, hour, minute);
        LocalDateTime startTime = endTime.minusDays(1L);

        List<BatchMemberSalesResDto> dto = memberSalesRepository.findByCreatedTimeBetween(startTime, endTime)
                .stream()
                .map(member -> {
                    return BatchMemberSalesResDto.builder()
                            .salesMemberName(member.getSalesMemberName())
                            .salesMemberCode(String.valueOf(member.getSalesMemberCode()))
                            .contractPrice(member.getContractPrice())
                            .contractCount(member.getContractCount())
                            .cancelPrice(member.getCancelPrice())
                            .cancelCount(member.getCancelCount())
                            .build();
                })
                .toList();

        return dto;
    }


}
