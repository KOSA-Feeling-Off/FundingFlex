package com.fundingflex.qa.chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatDTO, Long> {
}