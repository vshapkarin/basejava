package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class TimePeriodSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private List<TimePeriodOrganisation> content;

    public TimePeriodSection() {
    }

    public TimePeriodSection(TimePeriodOrganisation... content) {
        this(Arrays.asList(content));
    }

    public TimePeriodSection(List<TimePeriodOrganisation> content) {
        Objects.requireNonNull(content, "content must be not null");
        this.content = content;
    }

    public List<TimePeriodOrganisation> getContent() {
        return content;
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
