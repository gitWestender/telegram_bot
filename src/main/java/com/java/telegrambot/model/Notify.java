package com.java.telegrambot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
public class Notify {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "notify_msg", nullable = false)
    private String notifyMsg;

    @Column(name = "date_msg")
    private LocalDateTime dateMsg;
}
