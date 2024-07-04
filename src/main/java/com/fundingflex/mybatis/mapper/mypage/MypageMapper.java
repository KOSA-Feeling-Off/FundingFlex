package com.fundingflex.mybatis.mapper.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.member.domain.entity.Members;

@Mapper
public interface MypageMapper {

    @Select("SELECT * FROM members WHERE email = #{email}")
    Members findByEmail(@Param("email") String email);

    List<FundingsDTO> findParticipatedFundingsByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM funding_items WHERE creator_id = #{userId}")
    List<FundingsDTO> findCreatedFundingsByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM funding_items WHERE funding_id IN (SELECT funding_id FROM likes WHERE user_id = #{userId})")
    List<FundingsDTO> findLikedFundingsByUserId(@Param("userId") Long userId);
}
