package ru.javawebinar.basejava.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private final String uuid;
    private String fullName;
    private Map<String, Contact> contacts;
    private Map<SectionType, Section> sections;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
        contacts = new LinkedHashMap<>();
        sections = new LinkedHashMap<>();
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<String, Contact> getContacts() {
        return new LinkedHashMap<>(contacts);
    }

    public void setContacts(Map<String, Contact> contacts) {
        this.contacts = contacts;
    }

    public Map<SectionType, Section> getSections() {
        return new LinkedHashMap<>(sections);
    }

    public void setSections(Map<SectionType, Section> sections) {
        this.sections = sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.getUuid()) && fullName.equals(resume.getFullName());
    }

    @Override
    public int hashCode() {
        return uuid.hashCode() + fullName.hashCode();
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public int compareTo(Resume o) {
        int result = fullName.compareTo(o.getFullName());
        return result == 0 ? uuid.compareTo(o.getUuid()) : result;
    }
}
