//package com.fundingflex.qa.chat;
//
//import java.util.ArrayDeque;
//import java.util.Queue;
//
//import org.springframework.stereotype.Service;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class ChatService {
//
//    private final ChatRepository chatRepository;
//    private ChatServiceThread chatServiceThread;
//    private boolean runTread = false;
//
//    Queue<ChatDTO> chatQueue = new ArrayDeque<ChatDTO>();
//    
//    @PostConstruct
//    private void init() {
//        this.chatServiceThread = new ChatServiceThread(this, chatRepository);
//    }
//    
//    public boolean getRunTread() {
//    	return runTread;
//    }
//    
//    public void addChat(ChatDTO chat) {
//    	chatQueue.add(chat);
//    	if(!runTread) {
//    		saveChat();
//    	}
//    }
//    
//    public void saveChat() {
//    	Queue<ChatDTO> theadChatQueue = new ArrayDeque<ChatDTO>(chatQueue);
//    	int size = theadChatQueue.size();
//    	for(int i= 0; i < size; ++i) {
//    		chatQueue.remove();
//    	}
//    	 
//    	runTread = true;
//    	chatServiceThread.setTheadChatQueue(theadChatQueue);
//    	chatServiceThread.start();
//    }
//
//	public void endTread() {
//		runTread = false;
//	}
//    
//    
//}

package com.fundingflex.consultation.chat.service;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.fundingflex.consultation.chat.ChatRepository;
import com.fundingflex.consultation.chat.domain.ChatDTO;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private ExecutorService executorService;
    private boolean runThread = false;

    Queue<ChatDTO> chatQueue = new ArrayDeque<>();

    @PostConstruct
    private void init() {
        // 스레드 풀 초기화
        executorService = Executors.newFixedThreadPool(1);
    }

    public boolean getRunThread() {
        return runThread;
    }

    public void addChat(ChatDTO chat) {
        chatQueue.add(chat);
        if (!runThread) {
            saveChat();
        }
    }

    public void saveChat() {
        // 스레드 풀에서 작업을 실행
        if (executorService != null) {
            executorService.execute(() -> {
                runThread = true;
                Queue<ChatDTO> threadChatQueue = new ArrayDeque<>(chatQueue);
                int size = threadChatQueue.size();
                for (int i = 0; i < size; ++i) {
                    chatQueue.remove();
                }

                ChatServiceThread chatServiceThread = new ChatServiceThread(this, chatRepository);
                chatServiceThread.setThreadChatQueue(threadChatQueue);
                chatServiceThread.run();

                runThread = false;
            });
        } else {
            // 예외 처리: executorService가 초기화되지 않은 경우
            throw new IllegalStateException("ExecutorService is not initialized.");
        }
    }
}
