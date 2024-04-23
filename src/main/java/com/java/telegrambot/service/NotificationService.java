package com.java.telegrambot.service;

import com.java.telegrambot.model.Notify;
import com.java.telegrambot.repositories.NotificationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationsRepository notificationsRepository;

    public void addNotifyToRepo(Notify notify) {
        notificationsRepository.save(notify);
    }

    public Set<Notify> findAllByChatID(Long chatId) {
        return notificationsRepository.findAllByChatIdOrderById(chatId);
    }

//    public void deleteNotifyByID(Long id) {
//        notificationsRepository.deleteById(id);
//    }
}
