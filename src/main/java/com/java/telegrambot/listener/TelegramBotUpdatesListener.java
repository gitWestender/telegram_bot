package com.java.telegrambot.listener;

import com.java.telegrambot.model.Notify;
import com.java.telegrambot.service.NotificationService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final NotificationService notificationService;

    private final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private static final Pattern MSG_PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

    private static final String PREFIX = "/";
    private static final String DELIMETER = " ";
    private static final String START = "/start";
    private static final String ADD_NOTIFY = "/add";
    private static final String FIND_ALL_MY_NOTIFY = "/all";
    private static final String DELETE_NOTIFY = "/delete";
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



            if (msg.startsWith(PREFIX)) {

                String command = msg.substring(msg.indexOf(PREFIX), msg.indexOf(DELIMETER));
                String text = msg.substring(msg.indexOf(DELIMETER)).trim();
                Matcher matcher = MSG_PATTERN.matcher(text);

                switch (command) {
                    case START -> {
                        startCommand(chatId);
                        break;
                    }
                    case ADD_NOTIFY -> {
                        sendMessage(chatId,text);
                        if (matcher.matches()) {
                            String dateNotify = matcher.group(1);
                            String textNotify = matcher.group(3);
                            LocalDateTime localDateTime = LocalDateTime
                                    .parse(dateNotify, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                            Notify toRepo = new Notify(chatId, textNotify, localDateTime);
                            notificationService.addNotifyToRepo(toRepo);
                        }
                        break;
                    }
                    case FIND_ALL_MY_NOTIFY -> {
                        notificationService.findAllByChatID(chatId);
                        break;
                    }
//                case DELETE_NOTIFY -> {
//                    notificationService.deleteNotifyByID();
//                }
                    case HELP -> {
                        helpComand(chatId);
                        break;
                    }
                    default -> unknownCommand(chatId);
                }
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
                                
                /add - добавление напоминания
                /all - все напоминания (Work in progress)
                /delete - удаление напоминания (Work in progress)
                /help - получение справки
                """;
        sendMessage(chatId, text);
    }

    private void helpComand(Long chatId) {
        String text = """
                Для добавления напоминания (/add) введите сообщение вида:
                /all dd.mm.yyyy hh.mm Текст напоминания.
                                
                Для удаления напоминания (/delete) 
                предварительно воспользуйтесь поиском ваших напоминанй (/all)
                и удалите его по порядковому номеру.
                """;
        sendMessage(chatId, text);
    }

    private String cutTextMessageFromUpdate(String msg) {
        return null;
    }
}
