package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TextListSection extends AbstractSection {
    private List<String> content;

    public TextListSection(String... content) {
        Objects.requireNonNull(content, "content must be not null");
        this.content = Arrays.asList(content);
    }

    public List<String> getContent() {
        return new ArrayList<>(content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TextListSection section = (TextListSection) o;
        return content.equals(section.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}