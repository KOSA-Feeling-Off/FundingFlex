package com.fundingflex.mybatis.mapper.notification;

import com.fundingflex.notification.domain.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {

    // 알림 저장
    void insertNotification(Notification notification);
}
