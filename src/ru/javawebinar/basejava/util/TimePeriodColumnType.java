package ru.javawebinar.basejava.util;

public enum TimePeriodColumnType {
    ORGANISATION("Organisation: "),
    URL("Url: "),
    START_DATE("Start date: "),
    END_DATE("End date: "),
    TEXT("Text: "),
    OPTIONAL_TEXT("Optional text: ");

    private final String title;

    TimePeriodColumnType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getTitleLength() {
        return title.length();
    }
}
