package ru.javawebinar.basejava.model;

import java.time.LocalDate;

public class TimePeriod {
    private LocalDate start;
    private LocalDate end;
    private String name;
    private String text;

    public TimePeriod (LocalDate start, LocalDate end, String name, String text) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.text = text;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
