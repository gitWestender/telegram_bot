package com.java.telegrambot.repositories;

import com.java.telegrambot.model.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
}
