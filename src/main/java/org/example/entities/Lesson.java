package org.example.entities;

public class Lesson {

    private Time startTime;
    private Time endTime;
    private Subject subject;

    public Lesson(Time startTime, Time endTime, Subject subject) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
    }

    public Lesson(int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute, Subject subject) {
        this(new Time(startTimeHour, startTimeMinute), new Time(endTimeHour, endTimeMinute), subject);
    }

    public int getStartTimeHour() {
        return startTime.getHour();
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTime.setHour(startTimeHour);
    }

    public int getStartTimeMinute() {
        return startTime.getMinute();
    }

    public void setStartTimeMinute(int startTimeMinute) {
        this.startTime.setMinute(startTimeMinute);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public int getEndTimeMinute() {
        return endTime.getMinute();
    }

    public void setEndTimeMinute(int startTimeMinute) {
        this.endTime.setMinute(startTimeMinute);
    }

    public int getEndTimeHour() {
        return endTime.getHour();
    }

    public void setEndTimeHour(int startTimeHour) {
        this.endTime.setHour(startTimeHour);
    }

    public int getLengthInMinutes() {
        return (getEndTimeHour() - getStartTimeHour()) * 60 + ((getEndTimeMinute() - getStartTimeMinute() + 60) % 60);
    }

    public String getTimeRangeAsString(){
        return startTime.getAsString() + " - " + endTime.getAsString();
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", subject=" + subject +
                '}';
    }

    public enum Subject {
        BIOLOGY("????????????????"),
        MATH("??????????."),
        BELARUSIAN_LANGUAGE("??????. ????."),
        BELARUSIAN_LITERATURE("??????. ??????."),
        RUSSIAN_LANGUAGE("??????. ????."),
        RUSSIAN_LITERATURE("??????. ??????."),
        CHZS("??????"),
        PI("??????. ??. ?? ????."),
        CHEMISTRY("??????????"),
        GEOGRAPHY("??????????????????"),
        WORLD_HISTORY("????????????. ??????."),
        BELARUSIAN_HISTORY("??????. ??????."),
        ENGLISH_LANGUAGE("????????. ????."),
        PHYSICS("????????????"),
        IT("????????????."),
        DP_MP("????/????"),
        SOCIAL_STUDIES("??????????????????."),
        TECHNICAL_DRAWING("????????????????");

        private final String name;

        Subject(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Subject getSubjectByName(String name) {
            for (var constant : Subject.class.getEnumConstants())
                if (constant.toString().equals(name))
                    return constant;
            return null;
        }
    }
}
