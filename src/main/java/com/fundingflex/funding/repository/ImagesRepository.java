package com.fundingflex.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundingflex.funding.domain.entity.Images;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {

}
