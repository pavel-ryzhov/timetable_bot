package org.example.commands;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExtendedBotCommand;
import org.example.entities.Time;
import org.example.entities.Timetable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.example.general.Bot.timetable;

public class NowCommand extends ExtendedBotCommand {
    public NowCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void executeAsync(AbsSender absSender, User user, Chat chat, String[] strings, Logger logger) throws Exception {
        var day = timetable.getSchoolDayByDayOfWeek(Timetable.DayOfWeek.current());
        var time = Time.current();
        for (var lesson : day.getLessons()){
            if (Time.between(time, lesson.getStartTime(), lesson.getEndTime())){
                absSender.execute(new SendMessage(chat.getId().toString(), lesson.toString()));
                return;
            }
        }
        absSender.execute(new SendMessage(chat.getId().toString(), "Сейчас ничего не идёт."));
    }
}
