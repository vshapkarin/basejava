package ru.javawebinar.basejava.model;

import java.time.LocalDate;

public class TimePeriod {
    private LocalDate start;
    private LocalDate end;
    private String name;
    private String text;
    private String optionalText;

    public TimePeriod(LocalDate start, LocalDate end, String name, String text) {
        this(start, end, name, text, "");
    }

    public TimePeriod(LocalDate start, LocalDate end, String name, String text, String optionalText) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.text = text;
        this.optionalText = optionalText;
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

    public String getOptionalText() {
        return optionalText;
    }

    public void setOptionalText(String optionalText) {
        this.optionalText = optionalText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimePeriod period = (TimePeriod) o;
        return start.equals(period.start)
                && end.equals(period.end)
                && name.equals(period.name)
                && text.equals(period.text)
                && optionalText.equals(period.optionalText);
    }

    @Override
    public int hashCode() {
        return start.hashCode()
                + end.hashCode()
                + name.hashCode()
                + text.hashCode()
                + optionalText.hashCode();
    }
}
