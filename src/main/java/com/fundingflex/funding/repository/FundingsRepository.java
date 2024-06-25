package com.fundingflex.funding.repository;

import com.fundingflex.funding.domain.entity.Fundings;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FundingsRepository extends JpaRepository<Fundings, Long> {

}
