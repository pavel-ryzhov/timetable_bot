package org.example.commands;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExtendedBotCommand;
import org.example.entities.Timetable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.example.general.Bot.timetable;

public class TimetableCommand extends ExtendedBotCommand {
    public TimetableCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void executeAsync(AbsSender absSender, User user, Chat chat, String[] strings, Logger logger) throws Exception {
        var stringBuilder = new StringBuilder();
        var days = timetable.getSchoolDays();
        for (int i = 0; i < days.size(); i++) {
            stringBuilder.append(Timetable.DayOfWeek.getByIndex(i)).append('\n');
            var lessons = days.get(i).getLessons();
            for (int j = 0, lessonsSize = lessons.size(); j < lessonsSize; j++) {
                stringBuilder
                        .append(j + 1)
                        .append('.')
                        .append(' ')
                        .append(lessons.get(j).getTimeRangeAsString())
                        .append("    ")
                        .append(lessons.get(j).getSubject())
                        .append('\n');
            }
            stringBuilder.append('\n');
        }
        absSender.execute(new SendMessage(chat.getId().toString(), stringBuilder.toString()));
    }
}
