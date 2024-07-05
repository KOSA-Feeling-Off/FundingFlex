package com.fundingflex.notification.service;

import com.fundingflex.mybatis.mapper.member.MembersMapper;
import com.fundingflex.mybatis.mapper.notification.NotificationMapper;
import com.fundingflex.notification.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitterService sseEmitterService;
    private final NotificationMapper notificationMapper;
    private final MembersMapper membersMapper;

    // 알림 저장
    @Transactional
    public void insertNotification(Long fundingsId, Long userId, String message) {
        try {
            Notification notification = Notification.of(fundingsId, userId, message);
            notificationMapper.insertNotification(notification);
            log.info("Notification inserted for userId: {}, fundingsId: {}", userId, fundingsId);
            sseEmitterService.sendNotification(userId, notification);
        } catch (Exception e) {
            log.error("Error inserting notification for userId: {}, fundingsId: {}", userId, fundingsId, e);
            throw e; // 예외를 다시 던져 트랜잭션 롤백을 유도
        }
    }

    // 해당 유저의 알림 조회
    public List<Notification> selectNotification(Long userId) throws Exception {
        if (membersMapper.findById(userId) == null) {
            log.warn("User not found: {}", userId);
            throw new NotFoundException("존재하지 않는 유저입니다.");
        }

        try {
            List<Notification> notifications = notificationMapper.findByUserId(userId);
            log.info("Notifications retrieved for userId: {}", userId);
            return notifications;
        } catch (Exception e) {
            log.error("Error retrieving notifications for userId: {}", userId, e);
            throw e;
        }
    }
}