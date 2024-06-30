package com.fundingflex.consultation.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fundingflex.consultation.qa.service.QaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/qa")
@RequiredArgsConstructor
public class ChatController {

    private final QaService qaService;
	
    @GetMapping("/chat")
    public String liveChat() {
        return "qa/chat/chat";
    }

    @GetMapping("/chat-window")
    public String liveChatWindow() {
        return "qa/chat/chat-window";
    }

    @GetMapping("/chat-reply-window")
    public String liveChatReplyWindow() {
        return "qa/chat/chat-reply-window";
    }
}
