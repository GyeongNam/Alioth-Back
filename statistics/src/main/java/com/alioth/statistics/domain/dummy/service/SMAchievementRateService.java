package com.alioth.statistics.domain.dummy.service;

import com.alioth.statistics.domain.member.domain.SalesMembers;

import java.util.Map;

public interface SMAchievementRateService {

    Map<SalesMembers, String> achievementRatePercent();
    Map<SalesMembers, String> achievementRateCount();


}
