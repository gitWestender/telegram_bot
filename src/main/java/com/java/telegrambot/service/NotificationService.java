package com.java.telegrambot.service;

import com.java.telegrambot.model.Notify;
import com.java.telegrambot.repositories.NotificationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationsRepository notificationsRepository;

    public void addNotifyToRepo(Notify notify) {
        notificationsRepository.save(notify);
    }

    public String findAllByChatID(Long chatId) {
        List<Notify> notifies = notificationsRepository.findAllByChatIdOrderById(chatId);
        String sb = notifies.stream()
                .map(s -> s.msgToChat())
                .collect(Collectors.joining("\n"));
        System.out.println(sb);
        return sb;
    }

    public void deleteNotifyByID(Long id) {
        notificationsRepository.deleteById(id);
    }
}
