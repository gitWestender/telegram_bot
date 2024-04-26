package com.java.telegrambot.service;

import org.springframework.stereotype.Service;

@Service
public class NotifyMessageCutter {

    private static final String PREFIX = "/";

    public boolean isCommand(String text) {
        return text.startsWith(PREFIX);
    }

    public String getMessageFromText(String text) {
        String msgPart = "";
        if (text.contains(" ")) {
            int indexOfSpace = text.indexOf(" ");
            msgPart = text.substring(indexOfSpace).trim();
        }
        return msgPart;
    }

    public String getCommandFromText(String text) {
        String commandPart;
        if (text.contains(" ")) {
            int indexOfSpace = text.indexOf(" ");
            commandPart = text.substring(0, indexOfSpace + 1).toLowerCase().trim();
        } else {
            commandPart = text;
        }
        return commandPart;
    }
}
