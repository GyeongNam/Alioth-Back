package com.alioth.statistics.service;

import com.alioth.statistics.domain.dummy.domain.Custom;
import com.alioth.statistics.domain.dummy.repository.CustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberRankService implements StatisticsRankService {

    private final CustomRepository customRepository;

    @Override
    public Long rank() {
        List<Custom> all = customRepository.findAll();
        int size = all.size();
        long result = size;

        return result;
    }
}
