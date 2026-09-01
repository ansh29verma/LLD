package com.ansh.strategy;

import com.ansh.entity.ChannelType;



public class NotificationChannelFactory {
    public static NotificationChannel getChannel(ChannelType channelType) {
        return switch (channelType) {
            case EMAIL -> new EmailNotificationChannel();
            case SMS -> new SmsNotificationChannel();
            case PUSH -> new PushNotificationChannel();
        };
    }
}