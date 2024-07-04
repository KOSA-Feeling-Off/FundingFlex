package com.fundingflex.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundingflex.notification.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

    private final Map<Long, Map<String, SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    // SSE 생성
    public SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String emitterId = UUID.randomUUID().toString();
        userEmitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(emitterId, emitter);

        emitter.onCompletion(() -> userEmitters.get(userId).remove(emitterId));
        emitter.onTimeout(() -> userEmitters.get(userId).remove(emitterId));
        emitter.onError(e -> userEmitters.get(userId).remove(emitterId));

        return emitter;
    }

    // 알림 보내기
    public void sendNotification(Long userId, Notification notification) {
        Map<String, SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            for (SseEmitter emitter : emitters.values()) {
                try {
                    String json = new ObjectMapper().writeValueAsString(notification);
                    emitter.send(SseEmitter.event().name("notification").data(json));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }
        }
    }
}
