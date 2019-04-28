package ru.javawebinar.basejava.model;

import java.util.Objects;

public class Contact {
    private String content;

    public Contact(String content) {
        Objects.requireNonNull(content, "content must be not null");
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return content.equals(contact.content);
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
