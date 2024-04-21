package com.java.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pengrad.telegrambot.response.BaseResponse.*;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private static final String START = "/start";
    private static final String HELP = "/help";

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            LOGGER.info("Processing update: {}", update);

            String msg = update.message().text();
            Long chatId = update.message().chat().id();

            switch (msg) {
                case START -> {
                    startCommand(chatId);
                }
                default -> unknownCommand(chatId);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);

        telegramBot.execute(sendMessage);
    }

    private void unknownCommand(Long chatId) {
        String text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }

    private void startCommand(Long chatId) {
        String text = """
                Добро пожаловать в бот,
                
                ...
                Work in progress
                ...
                
                Дополнительные команды:
                /help - получение справки
                """;
        sendMessage(chatId, text);
    }
}
