package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TimePeriodSection extends AbstractSection {
    private List<TimePeriod> content;

    public TimePeriodSection (TimePeriod... content) {
        Objects.requireNonNull(content, "content must be not null");
        this.content = Arrays.asList(content);
    }

    public List<TimePeriod> getContent() {
        return new ArrayList<>(content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimePeriodSection section = (TimePeriodSection) o;

        return content.equals(section.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
