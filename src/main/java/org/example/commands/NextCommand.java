package org.example.commands;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExtendedBotCommand;
import org.example.entities.Lesson;
import org.example.entities.Time;
import org.example.entities.Timetable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Comparator;

import static org.example.general.Bot.timetable;

public class NextCommand extends ExtendedBotCommand {
    public NextCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void executeAsync(AbsSender absSender, User user, Chat chat, String[] strings, Logger logger) throws Exception {
        var dayIndex = Timetable.DayOfWeek.current().getIndex();
        var currentTime = Time.current();
        for (var i = dayIndex; i < dayIndex + 7; i++){
            var day = timetable.getSchoolDayByIndex(i % 7);
            if (day.hasLessons()){
                int finalI = i;
                var lesson = day.getLessons().stream().filter(l -> l.getStartTime().countDistance(currentTime, finalI - dayIndex) > 0).min(Comparator.comparing(Lesson::getStartTime)).orElse(null);
                if (lesson != null) {
                    absSender.execute(new SendMessage(chat.getId().toString(), String.valueOf(Timetable.DayOfWeek.getByIndex(i % 7)) + '\n' + lesson.getStartTime().getAsString() + " " + lesson.getSubject()));
                    break;
                }
            }
        }
    }
}
