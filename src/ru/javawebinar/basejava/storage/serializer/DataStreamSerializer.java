package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                SectionType title = entry.getKey();
                dos.writeUTF(title.name());
                switch (title) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(entry.getValue().toString());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeList(entry, dos);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeTimePeriod(entry, dos);
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
                    case "OBJECTIVE":
                        resume.addSection(SectionType.valueOf(sectionName), new TextOnlySection(dis.readUTF()));
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        resume.addSection(SectionType.valueOf(sectionName), new TextListSection(readList(dis)));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        resume.addSection(SectionType.valueOf(sectionName), new TimePeriodSection(readTimePeriod(dis)));
                        break;
                    default:
                        break;
                }
            }
            return resume;
        }
    }

    private void writeList(Map.Entry<SectionType, AbstractSection> entry, DataOutputStream dos) throws IOException {
        TextListSection section = (TextListSection) entry.getValue();
        dos.writeInt(section.getContent().size());
        for (String str : section.getContent()) {
            dos.writeUTF(str);
        }
    }

    private void writeTimePeriod(Map.Entry<SectionType, AbstractSection> entry, DataOutputStream dos) throws IOException {
        List<TimePeriodOrganisation> organisations = ((TimePeriodSection) entry.getValue()).getContent();
        dos.writeInt(organisations.size());
        for (TimePeriodOrganisation organisation : organisations) {
            dos.writeUTF(organisation.getHomePage().getName());
            String url = organisation.getHomePage().getUrl();
            if(url != null) {
                dos.writeUTF(url);
            } else {
                dos.writeUTF("");
            }

            List<TimePeriodOrganisation.TimePeriod> periods = organisation.getPeriods();
            dos.writeInt(periods.size());
            for (TimePeriodOrganisation.TimePeriod period : periods) {
                dos.writeUTF(period.getStart().toString());
                dos.writeUTF(period.getEnd().toString());
                dos.writeUTF(period.getText());
                String optionalText = period.getOptionalText();
                if(optionalText != null) {
                    dos.writeUTF(optionalText);
                } else {
                    dos.writeUTF("");
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
            String url = dis.readUTF();
            url = url.equals("") ? null : url;

            List<TimePeriodOrganisation.TimePeriod> periods = new ArrayList<>();
            int size2 = dis.readInt();
            for (int j = 0; j < size2; j++) {
                String optionalText;
                periods.add(new TimePeriodOrganisation.TimePeriod(
                        LocalDate.parse(dis.readUTF()),
                        LocalDate.parse(dis.readUTF()),
                        dis.readUTF(),
                        (optionalText = dis.readUTF()).equals("") ? null : optionalText
                ));
            }
            organisations.add(new TimePeriodOrganisation(new Contact(name, url), periods));
        }
        return organisations;
    }
}

