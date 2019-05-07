package ru.javawebinar.basejava.model;

import java.util.Objects;

public class TextOnlySection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private String content;

    public TextOnlySection(String content) {
        Objects.requireNonNull(content, "content must be not null");
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextOnlySection section = (TextOnlySection) o;

        return content.equals(section.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public String toString() {
        return content;
    }
}
