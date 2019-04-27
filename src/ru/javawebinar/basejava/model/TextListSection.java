package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextListSection extends Section {
    private List<String> content;

    public TextListSection(String... content) {
        this.content = Arrays.asList(content);

    }

    public List<String> getContent() {
        return new ArrayList<>(content);
    }

    public void setContent(List<String> content) {
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
        TextListSection section = (TextListSection) o;
        return content.equals(section.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
