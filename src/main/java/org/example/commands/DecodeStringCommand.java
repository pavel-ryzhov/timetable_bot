package org.example.commands;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExtendedBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.example.utils.Encoder.fromHexString;
import static org.example.utils.Encoder.decode;

/**
 * /decode_string <string>
 */
public class DecodeStringCommand extends ExtendedBotCommand {
    public DecodeStringCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description, false);
    }

    @Override
    public void executeAsync(AbsSender absSender, User user, Chat chat, String[] strings, Logger logger) throws Exception {
        absSender.execute(new SendMessage(chat.getId().toString(), new String(decode(fromHexString(strings[0])))));
    }
}
