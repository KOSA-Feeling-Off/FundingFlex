package com.fundingflex.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundingflex.funding.domain.entity.Fundings;

@Repository
public interface FundingsRepository extends JpaRepository<Fundings, Long> {

}
