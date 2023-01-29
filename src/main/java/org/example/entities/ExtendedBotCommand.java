package org.example.entities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Random;

import static org.example.general.Main.exceptionToString;
import static org.example.general.Bot.DEV_ID;

public abstract class ExtendedBotCommand extends BotCommand {

    private static final Logger LOGGER = LogManager.getLogger(ExtendedBotCommand.class);
    private static final Random RANDOM = new Random();

    private final boolean allowNonDeveloperUse;

    public ExtendedBotCommand(String commandIdentifier, String description, boolean allowNonDeveloperUse) {
        super(commandIdentifier, description);
        this.allowNonDeveloperUse = allowNonDeveloperUse;
    }
    public ExtendedBotCommand(String commandIdentifier, String description) {
        this(commandIdentifier, description, true);
    }

    public boolean isAllowNonDeveloperUse() {
        return allowNonDeveloperUse;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings){
        if (allowNonDeveloperUse || user.getId() == DEV_ID) {
            var thread = new Thread(() -> {
                try {
                    LOGGER.info(String.format("Execution of %s started.", this.getClass().getName()));
                    executeAsync(absSender, user, chat, strings, LogManager.getLogger(this.getClass()));
                    LOGGER.info(String.format("Execution of %s finished.", this.getClass().getName()));
                } catch (Exception e) {
                    LOGGER.warn(String.format("Exception during execution of %s!", this.getClass().getName()));
                    LOGGER.catching(Level.WARN, e);
                    try {
                        absSender.execute(new SendMessage(chat.getId().toString(), exceptionToString(e)));
                    } catch (TelegramApiException ex) {
                        LOGGER.error("Exception while sending message!");
                        LOGGER.catching(ex);
                    }
                }
            });
            thread.setName(this.getClass().getName() + "-" + RANDOM.nextInt(Integer.MAX_VALUE));
            thread.start();
        }
    }

    public abstract void executeAsync(AbsSender absSender, User user, Chat chat, String[] strings, Logger logger) throws Exception;
}
