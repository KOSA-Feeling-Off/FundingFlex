package com.fundingflex.notification.service;

import com.fundingflex.mybatis.mapper.member.MembersMapper;
import com.fundingflex.mybatis.mapper.notification.NotificationMapper;
import com.fundingflex.notification.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitterService sseEmitterService;
    private final NotificationMapper notificationMapper;
    private final MembersMapper membersMapper;

    // 알림 저장
    @Transactional
    public void insertNotification(Long fundingsId, Long userId, String message) {
        Notification notification = Notification.of(fundingsId, userId, message);
        notificationMapper.insertNotification(notification);
        sseEmitterService.sendNotification(userId, notification);
    }

    // 해당 유저의 알림 조회
    public List<Notification> selectNotification(Long userId) throws Exception {
        if (membersMapper.findById(userId) == null) {
            throw new NotFoundException("존재하지 않는 유저입니다.");
        }
        // 조회
        return notificationMapper.findByUserId(userId);
    }
}
