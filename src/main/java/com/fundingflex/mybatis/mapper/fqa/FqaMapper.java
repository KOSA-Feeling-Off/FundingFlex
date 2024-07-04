package com.fundingflex.mybatis.mapper.fqa;

import com.fundingflex.consultation.fqa.domain.dto.FqaDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FqaMapper {

    List<FqaDTO> findAll();

    FqaDTO findById(Long id);

    @Insert("INSERT INTO FQA (title, content, reply, created_at, admin_user_id) " +
            "VALUES (#{title}, #{content}, #{reply}, #{createdAt}, #{adminUserId})")
    void save(FqaDTO fqa);

    List<FqaDTO> findByTitleContainingIgnoreCase(String title);

    List<FqaDTO> findByContentContainingIgnoreCase(String content);

    List<FqaDTO> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String keyword);
}
