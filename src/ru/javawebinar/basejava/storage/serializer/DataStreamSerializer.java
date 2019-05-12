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
            forEachData(resume.getContacts().entrySet(), dos, a -> {
                dos.writeUTF(a.getKey().name());
                dos.writeUTF(a.getValue().toString());
            });

            forEachData(resume.getSections().entrySet(), dos, a -> {
                SectionType title = a.getKey();
                dos.writeUTF(title.name());
                switch (title) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(a.getValue().toString());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        forEachData(((TextListSection) a.getValue()).getContent(), dos, dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeTimePeriod(((TimePeriodSection) a.getValue()).getContent(), dos);
                        break;
                    default:
                        break;
                }
            });
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

    private void writeTimePeriod(List<TimePeriodOrganisation> organisations, DataOutputStream dos) throws IOException {
        UnaryOperator<String> nullWriter = a -> a != null ? a : "";

        forEachData(organisations, dos, a -> {
            dos.writeUTF(a.getHomePage().getName());
            dos.writeUTF(nullWriter.apply(a.getHomePage().getUrl()));

            forEachData(a.getPeriods(), dos, b -> {
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

    private <E> void forEachData(Collection<? extends E> collection, DataOutputStream dos, IOConsumer<? super E> action) throws IOException {
        Objects.requireNonNull(action);
        dos.writeInt(collection.size());
        for (E e : collection) {
            action.accept(e);
        }
    }
}

