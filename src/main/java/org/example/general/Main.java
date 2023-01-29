package org.example.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(new Bot());
        } catch (TelegramApiException e) {
            LOGGER.catching(e);
        }
    }
    public static String exceptionToString(Exception e) {
        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
