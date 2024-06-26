//package com.fundingflex.qa.chat;
//
//import java.util.Queue;
//
//public class ChatServiceThread extends Thread{
//
//	private final ChatService chatService;
//	private final ChatRepository chatRepository;
//	private Queue<ChatDTO> theadChatQueue;
//	
//	public ChatServiceThread(ChatService chatService, ChatRepository chatRepository) {
//		this.chatService = chatService;
//		this.chatRepository = chatRepository;
//	}
//	
//	public void setTheadChatQueue(Queue<ChatDTO> theadChatQueue) {
//		this.theadChatQueue = theadChatQueue;
//	}
//	
//    @Override
//    public void run() {
//    	
//    	while(chatService.getRunTread())
//    	{
//    		if(theadChatQueue.size() != 0) {
//    			chatRepository.save(theadChatQueue.remove());
//    		}
//    		else {
//    			chatService.endTread();
//    			break;
//    		}
//    	}
//    	
//    }
//	
//}

package com.fundingflex.consultation.chat.service;

import java.util.Queue;

import com.fundingflex.consultation.chat.ChatRepository;
import com.fundingflex.consultation.chat.domain.ChatDTO;

public class ChatServiceThread implements Runnable {

    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private Queue<ChatDTO> threadChatQueue;

    public ChatServiceThread(ChatService chatService, ChatRepository chatRepository) {
        this.chatService = chatService;
        this.chatRepository = chatRepository;
    }

    public void setThreadChatQueue(Queue<ChatDTO> threadChatQueue) {
        this.threadChatQueue = threadChatQueue;
    }

    @Override
    public void run() {
        while (!threadChatQueue.isEmpty()) {
            chatRepository.save(threadChatQueue.remove());
        }
    }
}
