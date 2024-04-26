package com.java.telegrambot.repositories;

import com.java.telegrambot.model.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notify, Long> {
    List<Notify> findAllByChatIdOrderById(Long chatId);
}
