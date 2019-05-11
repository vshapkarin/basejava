package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.IOConsumer;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.UnaryOperator;

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
                        writeList(((TextListSection) entry.getValue()).getContent(), dos);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeTimePeriod(((TimePeriodSection) entry.getValue()).getContent(), dos);
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
                SectionType section = SectionType.valueOf(sectionName);
                switch (sectionName) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        resume.addSection(section, new TextOnlySection(dis.readUTF()));
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        resume.addSection(section, new TextListSection(readList(dis)));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        resume.addSection(section, new TimePeriodSection(readTimePeriod(dis)));
                        break;
                    default:
                        break;
                }
            }
            return resume;
        }
    }

    private void writeList(List<String> listContent, DataOutputStream dos) throws IOException {
        dos.writeInt(listContent.size());
        forEachData(listContent, dos::writeUTF);
    }

    private void writeTimePeriod(List<TimePeriodOrganisation> organisations, DataOutputStream dos) throws IOException {
        UnaryOperator<String> nullWriter = a -> a != null ? a : "";

        dos.writeInt(organisations.size());
        forEachData(organisations, a -> {
            dos.writeUTF(a.getHomePage().getName());
            dos.writeUTF(nullWriter.apply(a.getHomePage().getUrl()));

            List<TimePeriodOrganisation.TimePeriod> periods = a.getPeriods();
            dos.writeInt(periods.size());
            forEachData(periods, b -> {
                dos.writeUTF(b.getStart().toString());
                dos.writeUTF(b.getEnd().toString());
                dos.writeUTF(b.getText());
                dos.writeUTF(nullWriter.apply(b.getOptionalText()));
            });
        });
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

    private <E> void forEachData(Collection<? extends E> collection, IOConsumer<? super E> action) throws IOException {
        Objects.requireNonNull(action);
        for (E e : collection) {
            action.accept(e);
        }
    }
}

