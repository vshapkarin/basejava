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
}
