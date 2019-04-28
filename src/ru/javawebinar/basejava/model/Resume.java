package ru.javawebinar.basejava.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private final String uuid;
    private String fullName;
    private Map<ContactType, Contact> contacts;
    private Map<SectionType, AbstractSection> sections;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must be not null");
        Objects.requireNonNull(fullName, "fullName must be not null");
        this.uuid = uuid;
        this.fullName = fullName;
        contacts = new EnumMap<>(ContactType.class);
        sections = new EnumMap<>(SectionType.class);
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public Map<ContactType, Contact> getContacts() {
        return new EnumMap<>(contacts);
    }

    public void setContacts(Map<ContactType, Contact> contacts) {
        Objects.requireNonNull(contacts, "contacts must be not null");
        this.contacts = contacts;
    }

    public Map<SectionType, AbstractSection> getSections() {
        return new EnumMap<>(sections);
    }

    public void setSections(Map<SectionType, AbstractSection> sections) {
        Objects.requireNonNull(sections, "sections must be not null");
        this.sections = sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.getUuid())
                && fullName.equals(resume.getFullName())
                && contacts.equals(resume.contacts)
                && sections.equals(resume.sections);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode()
                + fullName.hashCode()
                + contacts.hashCode()
                + sections.hashCode();
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
