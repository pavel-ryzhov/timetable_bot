package org.example.commands;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExtendedBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * /clear_logs <filename_filter>?
 */
public class ClearLogsCommand extends ExtendedBotCommand {
    public ClearLogsCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description, false);
    }

    @Override
    public void executeAsync(AbsSender absSender, User user, Chat chat, String[] strings, Logger logger) throws Exception {
        var files = Arrays.stream(Objects.requireNonNull(new File("logs").listFiles())).filter(file -> file.getName().contains(strings.length == 0 ? "" : strings[0])).collect(Collectors.toList());
        var c = 0;
        for (var file : files){
            if (file.delete()) c++;
        }
        absSender.execute(new SendMessage(chat.getId().toString(), c + " file" + (c == 1 ? "" : "s") + " were deleted."));
    }
}
