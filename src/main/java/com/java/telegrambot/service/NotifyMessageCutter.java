package com.java.telegrambot.service;

import org.springframework.stereotype.Service;

@Service
public class NotifyMessageCutter {

    private static final String PREFIX = "/";

    public boolean isCommand(String text) {
        return text.startsWith(PREFIX);
    }

    public String getMessageFromText(String text) {
        System.out.println("Полученный текст:" + text);
        String msgPart = "";
        if (text.contains(" ")) {
            int indexOfSpace = text.indexOf(" ");
            msgPart = text.substring(indexOfSpace).trim();
        }
        System.out.println("Возвращенный текст:" + msgPart);
        return msgPart;
    }

    public String getCommandFromText(String text) {
        System.out.println("Полученная команда:" + text);
        String commandPart = "";
        if (text.contains(" ")) {
            int indexOfSpace = text.indexOf(" ");
            commandPart = text.substring(0, indexOfSpace + 1).toLowerCase().trim();
        } else {
            commandPart = text;
        }
        System.out.println("Возвращенная команда:" + commandPart);
        return commandPart;
    }
}
