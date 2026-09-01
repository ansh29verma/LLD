package com.ansh.strategy;

import com.ansh.entity.Notification;

public interface NotificationChannel {
    void send(Notification notification);
}