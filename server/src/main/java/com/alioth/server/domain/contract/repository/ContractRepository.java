package com.alioth.server.domain.contract.repository;


import com.alioth.server.domain.contract.domain.Contract;
import com.alioth.server.domain.dummy.domain.Custom;
import com.alioth.server.domain.member.domain.SalesMembers;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findAllBySalesMembersId(Long memberId);

}
