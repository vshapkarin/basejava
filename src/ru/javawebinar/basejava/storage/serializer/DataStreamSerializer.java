package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.basejava.model.SectionType.*;

public class DataStreamSerializer implements SerializationStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, Contact> contacts = resume.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, Contact> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue().toString());
            }

            Map<SectionType, AbstractSection> sections = resume.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                switch (entry.getKey()) {
                    case PERSONAL:
                        writeText(entry, dos, PERSONAL.name());
                        break;
                    case OBJECTIVE:
                        writeText(entry, dos, OBJECTIVE.name());
                        break;
                    case ACHIEVEMENT:
                        writeList(entry, dos, ACHIEVEMENT.name());
                        break;
                    case QUALIFICATIONS:
                        writeList(entry, dos, QUALIFICATIONS.name());
                        break;
                    case EXPERIENCE:
                        writeTimePeriod(entry, dos, EXPERIENCE.name());
                        break;
                    case EDUCATION:
                        writeTimePeriod(entry, dos, EDUCATION.name());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String sectionName = dis.readUTF();
                switch (sectionName) {
                    case "PERSONAL":
                        resume.addSection(PERSONAL, new TextOnlySection(dis.readUTF()));
                        break;
                    case "OBJECTIVE":
                        resume.addSection(OBJECTIVE, new TextOnlySection(dis.readUTF()));
                        break;
                    case "ACHIEVEMENT":
                        resume.addSection(ACHIEVEMENT, new TextListSection(readList(dis)));
                        break;
                    case "QUALIFICATIONS":
                        resume.addSection(QUALIFICATIONS, new TextListSection(readList(dis)));
                        break;
                    case "EXPERIENCE":
                        resume.addSection(EXPERIENCE, new TimePeriodSection(readTimePeriod(dis)));
                        break;
                    case "EDUCATION":
                        resume.addSection(EDUCATION, new TimePeriodSection(readTimePeriod(dis)));
                        break;
                    default:
                        break;
                }
            }
            return resume;
        }
    }

    private void writeText(Map.Entry<SectionType, AbstractSection> entry, DataOutputStream dos, String title) throws IOException {
        dos.writeUTF(title);
        dos.writeUTF(entry.getValue().toString());
    }

    private void writeList(Map.Entry<SectionType, AbstractSection> entry, DataOutputStream dos, String title) throws IOException {
        dos.writeUTF(title);

        TextListSection section = (TextListSection) entry.getValue();
        dos.writeInt(section.getContent().size());
        for (String str : section.getContent()) {
            dos.writeUTF(str);
        }
    }

    private void writeTimePeriod(Map.Entry<SectionType, AbstractSection> entry, DataOutputStream dos, String title) throws IOException {
        dos.writeUTF(title);

        List<TimePeriodOrganisation> organisations = ((TimePeriodSection) entry.getValue()).getContent();
        dos.writeInt(organisations.size());
        for (TimePeriodOrganisation organisation : organisations) {
            dos.writeUTF(organisation.getHomePage().getName());
            String url = organisation.getHomePage().getUrl();
            if(url != null) {
                dos.writeBoolean(false);
                dos.writeUTF(url);
            } else {
                dos.writeBoolean(true);
            }

            List<TimePeriodOrganisation.TimePeriod> periods = organisation.getPeriods();
            dos.writeInt(periods.size());
            for (TimePeriodOrganisation.TimePeriod period : periods) {
                dos.writeUTF(period.getStart().toString());
                dos.writeUTF(period.getEnd().toString());
                dos.writeUTF(period.getText());
                String optionalText = period.getOptionalText();
                if(optionalText != null) {
                    dos.writeBoolean(false);
                    dos.writeUTF(optionalText);
                } else {
                    dos.writeBoolean(true);
                }
            }
        }
    }

    private List<String> readList(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(dis.readUTF());
        }
        return list;
    }

    private List<TimePeriodOrganisation> readTimePeriod(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        List<TimePeriodOrganisation> organisations = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String name = dis.readUTF();
            String url = dis.readBoolean() ? null : dis.readUTF();

            List<TimePeriodOrganisation.TimePeriod> periods = new ArrayList<>();
            int size2 = dis.readInt();
            for (int j = 0; j < size2; j++) {
                periods.add(new TimePeriodOrganisation.TimePeriod(
                        LocalDate.parse(dis.readUTF()),
                        LocalDate.parse(dis.readUTF()),
                        dis.readUTF(),
                        dis.readBoolean() ? null : dis.readUTF()
                ));
            }
            organisations.add(new TimePeriodOrganisation(new Contact(name, url), periods));
        }
        return organisations;
    }
}

