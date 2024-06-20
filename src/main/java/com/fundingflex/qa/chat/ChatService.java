package com.fundingflex.qa.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public ChatDTO save(ChatDTO chat) {
        return chatRepository.save(chat);
    }
}