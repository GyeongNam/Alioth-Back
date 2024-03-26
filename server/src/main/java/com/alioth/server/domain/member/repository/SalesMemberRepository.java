package com.alioth.server.domain.member.repository;

import com.alioth.server.domain.member.domain.SalesMembers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesMemberRepository extends JpaRepository<SalesMembers, Long> {


    SalesMembers findFirstByOrderByIdDesc();
    SalesMembers findBySalesMemberCode(Long salesMemberCode);
}
