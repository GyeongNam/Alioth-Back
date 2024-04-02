package com.alioth.statistics.domain.contract.repository;


import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findBySalesMembers(SalesMembers salesMembers);

}
