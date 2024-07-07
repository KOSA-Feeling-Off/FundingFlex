package com.fundingflex.mybatis.mapper.notification;

import com.fundingflex.notification.domain.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NotificationMapper {

    // 알림 저장
    void insertNotification(Notification notification);


    // 조회 (userId) 기준
    List<Notification> findByUserId(Long userId);


    // 읽음 상태 업데이트
    void updateNotificationReadStatus(@Param("userId") Long userId);
}
