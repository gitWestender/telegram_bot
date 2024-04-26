package com.java.telegrambot.listener;

import com.java.telegrambot.model.Notify;
import com.java.telegrambot.service.NotificationService;
import com.java.telegrambot.service.NotifyMessageCutter;
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
    private final NotifyMessageCutter cutter;

    private final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private static final Pattern MSG_PATTERN = Pattern.compile(
            "^(?<date>((0[1-9]|[1-3]\\d)\\.(0[1-9]|1[0-2])\\.20[2-9]\\d (0\\d|[1-2]\\d):(0[1-9]|[1-5]\\d))) (?<message>.+)$");
//            "([0-9\\.\\:\\s]{16})(\\s)([\\W+].+)");

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

            if (cutter.isCommand(msg)) {

                String cutCommand = cutter.getCommandFromText(msg);
                String cutText = cutter.getMessageFromText(msg);
                Matcher matcher = MSG_PATTERN.matcher(cutText);

                switch (cutCommand) {
                    case START -> startCommand(chatId);

                    case ADD_NOTIFY -> {
                        if (matcher.find()) {
                            String dateNotify = matcher.group("date");
                            String textNotify = matcher.group("message");
                            LocalDateTime localDateTime = LocalDateTime
                                    .parse(dateNotify, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                            Notify toRepo = new Notify(chatId, textNotify, localDateTime);
                            notificationService.addNotifyToRepo(toRepo);
                            sendMessage(chatId, "Напоминание добавлено");
                        }
                    }
                    case FIND_ALL_MY_NOTIFY -> sendMessage(chatId, notificationService.findAllByChatID(chatId));

                    case DELETE_NOTIFY -> {
                        notificationService.deleteNotifyByID(Long.parseLong(cutText));
                        sendMessage(chatId, "Напоминание удалено");
                    }
                    case HELP -> helpComand(chatId);

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
        String startText = """
                Добро пожаловать в бот,
                         
                /add - добавление напоминания
                /all - все напоминания
                /delete - удаление напоминания
                /help - получение справки
                """;
        sendMessage(chatId, startText);
    }

    private void helpComand(Long chatId) {
        String helpText = """
                Для добавления напоминания (/add) введите сообщение вида:
                /all dd.mm.yyyy hh.mm Текст напоминания.
                                
                Для удаления напоминания (/delete)
                предварительно воспользуйтесь поиском ваших напоминанй (/all)
                и удалите его по порядковому номеру.
                """;
        sendMessage(chatId, helpText);
    }
}
