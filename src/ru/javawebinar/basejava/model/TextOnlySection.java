package ru.javawebinar.basejava.model;

public class TextOnlySection extends Section {
    private String content;

    public TextOnlySection (String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
