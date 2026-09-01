package com.ansh.strategy;

import com.ansh.entity.Notification;

public class EmailNotificationChannel implements NotificationChannel {
    @Override
    public void send(Notification notification) {
        System.out.println(
                "Sending EMAIL to user " + notification.getUserId()
                        + ": " + notification.getMessage()
        );
    }
}
