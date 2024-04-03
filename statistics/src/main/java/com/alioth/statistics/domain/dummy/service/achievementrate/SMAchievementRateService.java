package com.alioth.statistics.domain.dummy.service.achievementrate;

import com.alioth.statistics.domain.member.domain.SalesMembers;

import java.util.Map;

public interface SMAchievementRateService {

    Map<SalesMembers, String> achievementRatePercent();
    Map<SalesMembers, String> achievementRateCount();


}
