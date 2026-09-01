package com.ansh;

import com.ansh.entity.ChannelType;
import com.ansh.entity.Notification;
import com.ansh.entity.UserPreference;
import com.ansh.service.AsyncNotificationService;
import com.ansh.service.NotificationDispatcher;
import com.ansh.service.NotificationService;
import com.ansh.service.UserPreferenceService;

import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // Defining preference service.
        UserPreferenceService preferenceService = new UserPreferenceService();

        // Defining user preference with Email and SMS as preferred channels.
        preferenceService.savePreference(
                new UserPreference(
                        "user123",
                        Set.of(ChannelType.EMAIL, ChannelType.SMS)
                )
        );

        // Defining notification dispatcher
        NotificationDispatcher dispatcher =
                new NotificationDispatcher(preferenceService);

        // Defining async service.
        AsyncNotificationService notificationService =
                new AsyncNotificationService(dispatcher);

        // Defining synchronous service.
        NotificationService service = new NotificationService(dispatcher);

        // Defining notification to send through multiple channels.
        Notification notification =
                new Notification(
                        "user123",
                        "Your order has been shipped!"
                );

        // Sending notification through synchronous service.
        service.sendNotification(notification);

        // Sending notification through asynchronous service.
        notificationService.sendNotification(notification);

    }
}