package com.fundingflex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundingflex.entity.FundingConditions;

@Repository
public interface FundingConditionsRepository extends JpaRepository<FundingConditions, Long> {
	FundingConditions findByFundingsId(Long fundingsId);
}