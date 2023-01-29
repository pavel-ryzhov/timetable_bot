package org.example.entities;

import java.util.List;

public class SchoolDay {
    private final List<Lesson> lessons;

    public SchoolDay(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public SchoolDay(Lesson... lessons) {
        this.lessons = List.of(lessons);
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public Lesson getLessonByIndex(int index){
        return lessons.get(index);
    }

    public Lesson getLessonByNumber(int index){
        return lessons.get(index + 1);
    }

    public boolean hasLessons(){
        return !lessons.isEmpty();
    }

    @Override
    public String toString() {
        return "SchoolDay{" +
                "lessons=" + lessons +
                '}';
    }
}
