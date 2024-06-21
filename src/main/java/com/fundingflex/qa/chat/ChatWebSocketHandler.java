//package com.fundingflex.qa.chat;
//
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//public class ChatWebSocketHandler extends TextWebSocketHandler{
//    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
//    private Set<WebSocketSession> customers = Collections.synchronizedSet(new HashSet<>());
//    private Set<WebSocketSession> agents = Collections.synchronizedSet(new HashSet<>());
//    private ConcurrentMap<WebSocketSession, WebSocketSession> connections = new ConcurrentHashMap<>();
//
//    private ChatService chatService;
//    
//    public ChatWebSocketHandler(ChatService chatService) {
//    	this.chatService = chatService;
//    }
//  
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        logger.info("새로운 WebSocket 연결 성공: " + session.getId());
//        // 세션을 대기 목록에 추가
//        // 고객은 "customer"로, 상담원은 "agent"로 설정
//        // 이 예제에서는 session에 사용자 유형을 저장하는 방식을 가정함
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        logger.info("수신한 메시지 [" + session.getId() + "]: " + payload);
//
//        // 메시지에 따른 처리 로직
//        // "REQUEST_CHAT" 메시지 처리
//        if (payload.equals("REQUEST_CHAT")) {
//            if (!agents.isEmpty()) {
//                WebSocketSession agent = agents.iterator().next();
//                connections.put(session, agent);
//                connections.put(agent, session);
//                agents.remove(agent);
//
//                session.sendMessage(new TextMessage("CONNECTED"));
//                agent.sendMessage(new TextMessage("CONNECTED"));
//                logger.info("고객과 상담원 연결됨: " + session.getId() + " -> " + agent.getId());
//
//                // 연결된 대화 정보를 데이터베이스에 저장
//                saveChatMessage(session, "상담이 시작되었습니다.");
//            } else {
//                customers.add(session);
//                session.sendMessage(new TextMessage("WAITING"));
//            }
//        }
//        // "READY_AGENT" 메시지 처리
//        else if (payload.equals("READY_AGENT")) {
//            if (!customers.isEmpty()) {
//                WebSocketSession customer = customers.iterator().next();
//                connections.put(session, customer);
//                connections.put(customer, session);
//                customers.remove(customer);
//
//                session.sendMessage(new TextMessage("CONNECTED"));
//                customer.sendMessage(new TextMessage("CONNECTED"));
//                logger.info("상담원과 고객 연결됨: " + session.getId() + " -> " + customer.getId());
//
//                // 연결된 대화 정보를 데이터베이스에 저장
//                saveChatMessage(session, "상담이 시작되었습니다.");
//            } else {
//                agents.add(session);
//                session.sendMessage(new TextMessage("WAITING"));
//            }
//        }
//        // 일반 메시지 전달
//        else {
//            WebSocketSession connectedSession = connections.get(session);
//            if (connectedSession != null && connectedSession.isOpen()) {
//                connectedSession.sendMessage(new TextMessage(payload));
//                // 대화 내용을 데이터베이스에 저장
//                saveChatMessage(session, payload);
//            }
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        logger.info("WebSocket 연결 종료: " + session.getId());
//        customers.remove(session);
//        agents.remove(session);
//
//        WebSocketSession connectedSession = connections.remove(session);
//        if (connectedSession != null) {
//            connections.remove(connectedSession);
//            if (connectedSession.isOpen()) {
//                connectedSession.sendMessage(new TextMessage("DISCONNECTED"));
//            }
//
//            // 연결 종료된 대화 정보를 데이터베이스에 저장
//            saveChatMessage(session, "상담이 종료되었습니다.");
//        }
//    }
//
//    private void saveChatMessage(WebSocketSession session, String message) {
//        ChatDTO chatDTO = new ChatDTO();
//        chatDTO.setContent(message);
//        chatDTO.setCreatedAt(new Date());
//       
//        if ("REQUEST_CHAT".equals(session.getAttributes().get("messageType"))) {
//        	chatDTO.setChatId(0L);
//            chatDTO.setIsAdmin("Y");
//        } 
//        else if ("READY_AGENT".equals(session.getAttributes().get("messageType"))) {
//        	chatDTO.setChatId(1L);
//            chatDTO.setIsAdmin("N");
//        }
//        else {
//            logger.warn("Unknown message type in session: " + session.getAttributes().get("messageType"));
//            return;
//        }
//        
//        try {
//            chatService.save(chatDTO);
//        } catch (Exception e) {
//            logger.error("Error saving chat message to database.", e);
//        }
//    }
//}

package com.fundingflex.qa.chat;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private Set<WebSocketSession> customers = Collections.synchronizedSet(new HashSet<>());
    private Set<WebSocketSession> agents = Collections.synchronizedSet(new HashSet<>());
    private ConcurrentMap<WebSocketSession, WebSocketSession> connections = new ConcurrentHashMap<>();

    private ChatService chatService;

    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("새로운 WebSocket 연결 성공: " + session.getId());
        
        // 세션에 상담 번호 저장 (임의로 예시로 100번으로 설정)
        session.getAttributes().put("chatNumber", 100L);
        session.getAttributes().put("userType", "basic");
        //logger.info("afterConnectionEstablished: " + (String)session.getAttributes().get("userType"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        logger.info("수신한 메시지 [" + session.getId() + "]: " + payload);

        // 메시지에 따른 처리 로직
        // "REQUEST_CHAT" 메시지 처리
        if (payload.equals("REQUEST_CHAT")) {
        	session.getAttributes().put("userType", "customer");
        	//logger.info("handleTextMessage: " + (String)session.getAttributes().get("userType"));
            if (!agents.isEmpty()) {
            	
            	WebSocketSession agent = agents.iterator().next();
                connections.put(session, agent);
                connections.put(agent, session);
                agents.remove(agent);

                
                session.sendMessage(new TextMessage("CONNECTED"));
                agent.sendMessage(new TextMessage("CONNECTED"));
                //logger.info("고객과 상담원 연결됨: " + session.getId() + " -> " + agent.getId());
            } 
            else {
                customers.add(session);
                session.sendMessage(new TextMessage("WAITING"));
            }
        }
        // "READY_AGENT" 메시지 처리
        else if (payload.equals("READY_AGENT")) {
        	session.getAttributes().put("userType", "agent");
        	//logger.info("handleTextMessage: " + (String)session.getAttributes().get("userType"));
            if (!customers.isEmpty()) {

            	WebSocketSession customer = customers.iterator().next();
                connections.put(session, customer);
                connections.put(customer, session);
                customers.remove(customer);

                
                session.sendMessage(new TextMessage("CONNECTED"));
                customer.sendMessage(new TextMessage("CONNECTED"));
                //logger.info("상담원과 고객 연결됨: " + session.getId() + " -> " + customer.getId());

            } 
            else {
                agents.add(session);
                session.sendMessage(new TextMessage("WAITING"));
            }
            saveChatMessage(session, "No." + session.getAttributes().get("chatNumber") + " 상담 시작");
        }
        // 일반 메시지 전달
        else {
            WebSocketSession connectedSession = connections.get(session);
            if (connectedSession != null && connectedSession.isOpen()) {
                connectedSession.sendMessage(new TextMessage(payload));
                // 대화 내용을 데이터베이스에 저장
                saveChatMessage(session, payload);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("WebSocket 연결 종료: " + session.getId());
        customers.remove(session);
        agents.remove(session);

        WebSocketSession connectedSession = connections.remove(session);
        if (connectedSession != null) {
            connections.remove(connectedSession);
            if (connectedSession.isOpen()) {
                connectedSession.sendMessage(new TextMessage("DISCONNECTED"));
            }

            saveChatMessage(session, "No." + session.getAttributes().get("chatNumber") + " 상담 종료");
            //혹시 남아있을 채팅 저장
            chatService.saveChat();
        }
    }

    private void saveChatMessage(WebSocketSession session, String message) {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setContent(message);
        chatDTO.setCreatedAt(new Date());
        
        // 세션에서 상담 번호 읽어오기
        Long chatNumber = (Long) session.getAttributes().get("chatNumber");
        chatDTO.setChatNumber(chatNumber);
        
        // 세션에서 사용자 유형 읽어오기
        String userType = (String) session.getAttributes().get("userType");
        
        logger.info("saveChatMessage1: " + (String)session.getAttributes().get("userType"));
        logger.info("saveChatMessage2: " + userType);
        
        if (userType.equals("agent")) {
        	chatDTO.setUserId(0L);
            chatDTO.setIsAdmin("Y");
        }
        else if (userType.equals("customer")) {
        	chatDTO.setUserId(1L);
            chatDTO.setIsAdmin("N");
        } 
        else {
            logger.warn("Unknown user type in session: " + userType);
            return;
        }
        
        try {
            chatService.addChat(chatDTO);
        } catch (Exception e) {
            logger.error("Error saving chat message to database.", e);
        }
    }
}
