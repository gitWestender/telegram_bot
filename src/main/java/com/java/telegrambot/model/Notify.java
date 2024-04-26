package com.java.telegrambot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Table(name = "notifications")
public class Notify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "notify_msg", nullable = false)
    private String notifyMsg;

    @Column(name = "date_msg")
    private LocalDateTime dateMsg;

    public Notify() {
    }

    public Notify(Long chatId, String notifyMsg, LocalDateTime dateMsg) {
        this.chatId = chatId;
        this.notifyMsg = notifyMsg;
        this.dateMsg = dateMsg;
    }

    public String msgToChat() {
        return id +
                ". " + dateMsg +
                " " + notifyMsg +
                " " + dateMsg;
    }
}
