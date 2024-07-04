package com.fundingflex.mybatis.mapper.qa;

import com.fundingflex.consultation.qa.domain.dto.QaDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QaMapper {

	List<QaDTO> findByMembersUserId(@Param("membersUserId") Long membersUserId,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow);

	int countByMembersUserId(@Param("membersUserId") Long membersUserId);

	QaDTO findById(@Param("id") Long id);
	QaDTO findLastByMembersUserId(@Param("membersUserId") Long membersUserId);
    void save(QaDTO qaDTO);

    void update(QaDTO qaDTO);

    void deleteById(@Param("id") Long id);

	List<QaDTO> findByAllQuestions(@Param("startRow") int startRow, @Param("endRow") int endRow);
}
