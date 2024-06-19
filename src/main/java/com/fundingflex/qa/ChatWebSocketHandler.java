package com.fundingflex.qa;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private Set<WebSocketSession> customers = Collections.synchronizedSet(new HashSet<>());
    private Set<WebSocketSession> agents = Collections.synchronizedSet(new HashSet<>());
    private ConcurrentMap<WebSocketSession, WebSocketSession> connections = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("새로운 WebSocket 연결 성공: " + session.getId());
        // 세션을 대기 목록에 추가
        // 고객은 "customer"로, 상담원은 "agent"로 설정
        // 이 예제에서는 session에 사용자 유형을 저장하는 방식을 가정함
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        logger.info("수신한 메시지 [" + session.getId() + "]: " + payload);

        // 메시지에 따른 처리 로직
        // "REQUEST_CHAT" 메시지 처리
        if (payload.equals("REQUEST_CHAT")) {
            if (!agents.isEmpty()) {
                WebSocketSession agent = agents.iterator().next();
                connections.put(session, agent);
                connections.put(agent, session);
                agents.remove(agent);

                session.sendMessage(new TextMessage("CONNECTED"));
                agent.sendMessage(new TextMessage("CONNECTED"));
                logger.info("고객과 상담원 연결됨: " + session.getId() + " -> " + agent.getId());
            } else {
                customers.add(session);
                session.sendMessage(new TextMessage("WAITING"));
            }
        }
        // "READY_AGENT" 메시지 처리
        else if (payload.equals("READY_AGENT")) {
            if (!customers.isEmpty()) {
                WebSocketSession customer = customers.iterator().next();
                connections.put(session, customer);
                connections.put(customer, session);
                customers.remove(customer);

                session.sendMessage(new TextMessage("CONNECTED"));
                customer.sendMessage(new TextMessage("CONNECTED"));
                logger.info("상담원과 고객 연결됨: " + session.getId() + " -> " + customer.getId());
            } else {
                agents.add(session);
                session.sendMessage(new TextMessage("WAITING"));
            }
        }
        // 일반 메시지 전달
        else {
            WebSocketSession connectedSession = connections.get(session);
            if (connectedSession != null && connectedSession.isOpen()) {
                connectedSession.sendMessage(new TextMessage(payload));
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
        }
    }
}