package com.fundingflex.mybatis.mapper.pay;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.pay.domain.dto.PayDTO;

@Mapper
public interface PayMapper {
	
	public String getFundingsTitleByJoinId(@Param("fundingJoinId") Long fundingJoinId);
	public Long getFundingJoinsFundingAmountByJoinId(@Param("fundingJoinId") Long fundingJoinId);
	
    @Insert("INSERT INTO PAYMENTS (funding_join_id, user_id, pay_amount, pay_uuid, pay_time, is_deleted) " +
            "VALUES (#{fundingJoinId}, #{userId}, #{payAmount}, #{payUuid}, #{payTime,jdbcType=TIMESTAMP},  #{isDeleted})")
	public void save(PayDTO payDTO);
	public void updatePaymentsIsDeleted(Long fundingJoinsId);
	public void updateFundingJoinsIsDeleted(Long fundingJoinsId);
}
