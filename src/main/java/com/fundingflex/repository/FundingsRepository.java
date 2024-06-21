package com.fundingflex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundingflex.entity.Fundings;

@Repository
public interface FundingsRepository extends JpaRepository<Fundings, Long> {
}