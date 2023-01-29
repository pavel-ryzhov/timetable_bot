package org.example.commands;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExtendedBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.stream.Collectors;

import static org.example.general.Bot.commands;
import static org.example.general.Bot.DEV_ID;
import static org.example.general.Bot.BOT_USERNAME;

public class CommandsCommand extends ExtendedBotCommand {
    public CommandsCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void executeAsync(AbsSender absSender, User user, Chat chat, String[] strings, Logger logger) throws Exception {
        var identifiers = commands.stream().filter(iBotCommand -> ((ExtendedBotCommand) iBotCommand).isAllowNonDeveloperUse() || user.getId() == DEV_ID).map(IBotCommand::getCommandIdentifier).filter(identifier -> !identifier.contains(BOT_USERNAME)).collect(Collectors.toList());
        var stringBuilder = new StringBuilder();
        for (var identifier : identifiers) stringBuilder.append('/').append(identifier).append('\n');
        absSender.execute(new SendMessage(chat.getId().toString(), stringBuilder.toString()));
    }
}
