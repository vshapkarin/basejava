package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class TimePeriod {
    private Link homePage;
    private LocalDate start;
    private LocalDate end;
    private String text;
    private String optionalText;

    public TimePeriod(String name, String url, LocalDate start, LocalDate end, String text) {
        this(name, url, start, end, text, "");
    }

    public TimePeriod(String name, String url, LocalDate start, LocalDate end, String text, String optionalText) {
        Objects.requireNonNull(start, "start date must not be null");
        Objects.requireNonNull(end, "end date must not be null");
        Objects.requireNonNull(name, "organisation name must not be null");
        Objects.requireNonNull(text, "titul text must not be null");
        this.homePage = new Link(name, url);
        this.start = start;
        this.end = end;
        this.text = text;
        this.optionalText = optionalText;
    }

    public Link getHomePage() {
        return homePage;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public String getText() {
        return text;
    }

    public String getOptionalText() {
        return optionalText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimePeriod period = (TimePeriod) o;

        if (!homePage.equals(period.homePage)) return false;
        if (!start.equals(period.start)) return false;
        if (!end.equals(period.end)) return false;
        if (!text.equals(period.text)) return false;
        return optionalText != null ? optionalText.equals(period.optionalText) : period.optionalText == null;

    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + (optionalText != null ? optionalText.hashCode() : 0);
        return result;
    }
}
