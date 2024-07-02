package com.fundingflex.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fundingflex.mybatis.mapper.notification.NotificationMapper;
import com.fundingflex.notification.domain.entity.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;


    // 알림 저장
    @Transactional
    public void insertNotification(Long fundingsId, Long userId, String message) {
        Notification notification = Notification.of(fundingsId, userId, message);

        notificationMapper.insertNotification(notification);
    }
}
