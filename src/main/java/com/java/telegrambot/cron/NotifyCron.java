package com.java.telegrambot.cron;

import com.java.telegrambot.service.NotificationService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class NotifyCron {
    private final TelegramBot telegramBot;
    private final NotificationService notificationService;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void doReminder() {
        notificationService.findAllByDateMsg(LocalDateTime.now()
                        .truncatedTo(ChronoUnit.MINUTES))
                .forEach(reminder -> {
                    telegramBot.execute(new SendMessage(reminder.getChatId(),
                            "Напоминаю!\n" + reminder.getNotifyMsg()));
                });
    }
}
