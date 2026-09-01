package com.ansh.service;

import com.ansh.entity.Notification;

public class NotificationService {
    private final NotificationDispatcher dispatcher;

    public NotificationService(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void sendNotification(Notification notification) {
        dispatcher.dispatch(notification);
    }
}