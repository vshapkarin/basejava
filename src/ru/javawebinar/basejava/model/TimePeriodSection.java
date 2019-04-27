package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimePeriodSection extends Section {
    private List<TimePeriod> content;

    public TimePeriodSection (TimePeriod... content) {
        this.content = Arrays.asList(content);
    }

    public List<TimePeriod> getContent() {
        return new ArrayList<>(content);
    }

    public void setContent(List<TimePeriod> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimePeriodSection section = (TimePeriodSection) o;
        return content.equals(section.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
