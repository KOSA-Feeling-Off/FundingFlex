package com.fundingflex.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundingflex.notification.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseEmitterService {

    private final Map<Long, Map<String, SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    // SSE 생성
    public SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String emitterId = UUID.randomUUID().toString();
        userEmitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(emitterId, emitter);

        emitter.onCompletion(() -> {
            log.info("Emitter {} completed", emitterId);
            removeEmitter(userId, emitterId);
        });
        emitter.onTimeout(() -> {
            log.warn("Emitter {} timed out", emitterId);
            removeEmitter(userId, emitterId);
        });
        emitter.onError(e -> {
            log.error("Emitter {} error: {}", emitterId, e.getMessage());
            removeEmitter(userId, emitterId);
        });

        return emitter;
    }

    // 알림 보내기
    public void sendNotification(Long userId, Notification notification) {
        Map<String, SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            for (String emitterId : emitters.keySet()) {
                SseEmitter emitter = emitters.get(emitterId);
                try {
                    String json = new ObjectMapper().writeValueAsString(notification);
                    emitter.send(SseEmitter.event().name("notification").data(json));
                } catch (IOException e) {
                    log.error("Error sending notification to emitter {}: {}", emitterId, e.getMessage());
                    emitter.completeWithError(e); // 비동기 완료 처리
                    removeEmitter(userId, emitterId); // 추가: 예외 발생 시 emitter 제거
                }
            }
        } else {
            log.warn("No emitters found for user {}", userId);
        }
    }

    private synchronized void removeEmitter(Long userId, String emitterId) {
        Map<String, SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            emitters.remove(emitterId);
            if (emitters.isEmpty()) {
                userEmitters.remove(userId);
            }
        }
    }
}