package org.example.commands;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExtendedBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * /get_logs <filename_filter>?
 */
public class GetLogsCommand extends ExtendedBotCommand {
    public GetLogsCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description, false);
    }

    @Override
    public void executeAsync(AbsSender absSender, User user, Chat chat, String[] strings, Logger logger) throws Exception {
        var files = Arrays.stream(Objects.requireNonNull(new File("logs").listFiles())).filter(file -> file.getName().contains(strings.length == 0 ? "" : strings[0])).collect(Collectors.toList());
        for (var file : files){
            absSender.execute(new SendDocument(chat.getId().toString(), new InputFile(file)));
        }
        absSender.execute(new SendMessage(chat.getId().toString(), files.size() == 0 ? "There are no logs yet." : "Logs are sent."));
    }
}
