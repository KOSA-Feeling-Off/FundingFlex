package com.fundingflex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundingflex.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}