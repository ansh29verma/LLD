package com.ansh.strategy;

import com.ansh.entity.Notification;

public class SmsNotificationChannel implements NotificationChannel {
    @Override
    public void send(Notification notification) {
        System.out.println(
                "Sending SMS to user " + notification.getUserId()
                        + ": " + notification.getMessage()
        );
    }
}
