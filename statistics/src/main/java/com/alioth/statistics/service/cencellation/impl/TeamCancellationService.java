package com.alioth.statistics.service.cencellation.impl;

import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.service.cencellation.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeamCancellationService implements CancellationService {
    @Override
    public Map<Team, String> cancelMoneyPercent() {


        return null;
    }

    @Override
    public Map<Team, String> cancelCountPercent() {


        return null;
    }
}
