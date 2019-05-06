package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TimePeriodSection extends AbstractSection {
    private List<TimePeriodOrganisation> content;

    public TimePeriodSection(TimePeriodOrganisation... content) {
        this(Arrays.asList(content));
    }

    public TimePeriodSection(List<TimePeriodOrganisation> content) {
        Objects.requireNonNull(content, "content must be not null");
        this.content = content;
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

    @Override
    public String toString() {
        return content.toString();
    }
}
