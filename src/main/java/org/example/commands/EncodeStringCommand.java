package org.example.commands;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExtendedBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.example.utils.Encoder.encode;
import static org.example.utils.Encoder.toHexString;

/**
 * /encode_string <string>
 */
public class EncodeStringCommand extends ExtendedBotCommand {
    public EncodeStringCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description, false);
    }

    @Override
    public void executeAsync(AbsSender absSender, User user, Chat chat, String[] strings, Logger logger) throws Exception {
        absSender.execute(new SendMessage(chat.getId().toString(), toHexString(encode(strings[0].getBytes()))));
    }
}
