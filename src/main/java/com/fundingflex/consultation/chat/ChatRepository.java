package com.fundingflex.consultation.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundingflex.consultation.chat.domain.ChatDTO;

public interface ChatRepository extends JpaRepository<ChatDTO, Long> {
}