package com.fundingflex.notification.controller;

import com.fundingflex.member.domain.dto.CustomUserDetails;
import com.fundingflex.notification.domain.entity.Notification;
import com.fundingflex.notification.service.NotificationService;
import com.fundingflex.notification.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final SseEmitterService sseEmitterService;
    private final NotificationService notificationService;

    // 알림 조회
    @GetMapping("/list")
    public ResponseEntity<List<Notification>> listNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<Notification> notificationList = notificationService.selectNotification(userDetails.getUserId());
            if (notificationList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(notificationList);
        } catch (Exception ex) {
            log.error("Error fetching notifications for user: {}", userDetails.getUserId(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 알림 browser 로 push
    @GetMapping
    public ResponseEntity<SseEmitter> streamNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        try {
            SseEmitter emitter = sseEmitterService.createEmitter(userDetails.getUserId());
            return ResponseEntity.ok(emitter);
        } catch (Exception ex) {
            log.error("Error creating SSE emitter for user: {}", userDetails.getUserId(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}