package com.alioth.statistics.domain.contract.repository;


import com.alioth.statistics.domain.contract.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
}
