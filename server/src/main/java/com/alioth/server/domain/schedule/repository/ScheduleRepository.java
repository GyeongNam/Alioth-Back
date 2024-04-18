package com.alioth.server.domain.schedule.repository;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s WHERE s.salesMembers = :salesMember AND s.scheduleDel_YN = :scheduleDel_YN")
    List<Schedule> findAllBySalesMembersAndScheduleDel_YN(@Param("salesMember") SalesMembers salesMember, @Param("scheduleDel_YN") String scheduleDel_YN);
}
