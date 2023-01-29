package org.example.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.commands.*;
import org.example.entities.Timetable;
import org.example.utils.SchoolsByParser;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bot extends TelegramLongPollingCommandBot {

    public static final long DEV_ID = 1191761157;
    public static final long DEFAULT_CHAT = -726435377;

    private static final String TOKEN = "5976012446:AAHQf18LPWkRC1-U0KNv6AwAX3UbeerH1MA";
    public static final String BOT_USERNAME = "@timetable_1111_bot";

    private static final Logger LOGGER = LogManager.getLogger(Bot.class);

    private static final SchoolsByParser SCHOOLS_BY_PARSER = new SchoolsByParser("Ryzhov Pavel", "21092006");
    public static Timetable timetable;
    public static List<IBotCommand> commands = new ArrayList<>();

    public Bot() {
        try {
            timetable = SCHOOLS_BY_PARSER.getTimetable();
        } catch (IOException e) {
            LOGGER.catching(e);
        }
        registerCommand(TimetableCommand.class, "timetable", "/timetable");
        registerCommand(NowCommand.class, "now", "/now");
        registerCommand(NextCommand.class, "next", "/next");
        registerCommand(GetLogsCommand.class, "get_logs", "/get_logs <filename_filter>?");
        registerCommand(ClearLogsCommand.class, "clear_logs", "/clear_logs <filename_filter>?");
        registerCommand(EncodeStringCommand.class, "encode_string", "/encode_string <string>");
        registerCommand(DecodeStringCommand.class, "decode_string", "/decode_string <string>");
        registerCommand(CommandsCommand.class, "commands", "");

        commands.addAll(getRegisteredCommands());
    }

    public void registerCommand(Class<? extends IBotCommand> commandClass, String identifier, String description) {
        try {
            var constructor = commandClass.getConstructor(String.class, String.class);
            register(constructor.newInstance(identifier, description));
            register(constructor.newInstance(identifier + BOT_USERNAME, description));
        } catch (InvocationTargetException ignored) {
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            LOGGER.catching(e);
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        System.out.println(update);
    }

    @Override
    public boolean filter(Message message) {
        return System.currentTimeMillis() / 1000 - message.getDate() > 300;
    }
}
