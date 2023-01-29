package org.example.entities;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Timetable {
    private final List<SchoolDay> schoolDays;

    public Timetable(List<SchoolDay> schoolDays) {
        this.schoolDays = schoolDays;
    }

    public Timetable(SchoolDay... schoolDays) {
        this.schoolDays = List.of(schoolDays);
    }

    public List<SchoolDay> getSchoolDays() {
        return schoolDays;
    }

    public SchoolDay getSchoolDayByIndex(int index) {
        return index < schoolDays.size() ? schoolDays.get(index) : new SchoolDay();
    }

    public SchoolDay getSchoolDayByDayOfWeek(DayOfWeek dayOfWeek) {
        return getSchoolDayByIndex(dayOfWeek.getIndex());
    }

    public enum DayOfWeek {
        MONDAY("Понедельник"), TUESDAY("Вторник"), WEDNESDAY("Среда"), THURSDAY("Четверг"), FRIDAY("Пятница"), SATURDAY("Суббота"), SUNDAY("Воскресенье");

        private final String name;

        DayOfWeek(String name) {
            this.name = name;
        }

        public int getIndex() {
            var values = DayOfWeek.values();
            for (int i = 0; i < values.length; i++) {
                if (values[i].equals(this)) return i;
            }
            return -1;
        }

        @Override
        public String toString() {
            return name;
        }

        public static DayOfWeek getByIndex(int index) {
            return DayOfWeek.values()[index];
        }

        public static DayOfWeek current() {
            return values()[(new GregorianCalendar().get(Calendar.DAY_OF_WEEK) + 5) % 7];
        }
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "schoolDays=" + schoolDays +
                '}';
    }
}
