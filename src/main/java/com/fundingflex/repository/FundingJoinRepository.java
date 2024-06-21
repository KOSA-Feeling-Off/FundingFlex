package com.fundingflex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundingflex.entity.FundingJoin;

@Repository
public interface FundingJoinRepository extends JpaRepository<FundingJoin, Long> {
	List<FundingJoin> findByFundingsId(Long fundingsId);
}
