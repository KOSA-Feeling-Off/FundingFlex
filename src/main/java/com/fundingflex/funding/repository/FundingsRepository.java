/*
package com.fundingflex.funding.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundingflex.funding.domain.entity.Fundings;
import com.fundingflex.funding.domain.enums.FundingsStatusEnum;

@Repository
@Mapper
public interface FundingsRepository extends JpaRepository<Fundings, Long> {
    List<Fundings> findAllByOrderByCreatedAtDesc();
    List<Fundings> findAllByOrderByLikeCountDesc();
    List<Fundings> findAllByStatusFlag(FundingsStatusEnum statusFlag);
}
*/