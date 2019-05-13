package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.IOConsumer;
import ru.javawebinar.basejava.util.IORunnable;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class DataStreamSerializer implements SerializationStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            forEachWrite(resume.getContacts().entrySet(), dos, a -> {
                dos.writeUTF(a.getKey().name());
                dos.writeUTF(a.getValue().toString());
            });

            forEachWrite(resume.getSections().entrySet(), dos, a -> {
                SectionType title = a.getKey();
                dos.writeUTF(title.name());
                switch (title) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(a.getValue().toString());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        forEachWrite(((TextListSection) a.getValue()).getContent(), dos, dos::writeUTF);
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
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            forRead(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            forRead(dis, () -> {
                SectionType section = SectionType.valueOf(dis.readUTF());
                switch (section.name()) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        resume.addSection(section, new TextOnlySection(dis.readUTF()));
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        resume.addSection(section, new TextListSection(forListRead(dis, list -> list.add(dis.readUTF()))));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        resume.addSection(section, new TimePeriodSection(readTimePeriod(dis)));
                        break;
                    default:
                        break;
                }
            });
            return resume;
        }
    }

    private void writeTimePeriod(List<TimePeriodOrganisation> organisations, DataOutputStream dos) throws IOException {
        UnaryOperator<String> nullWriter = a -> a != null ? a : "";

        forEachWrite(organisations, dos, organisation -> {
            dos.writeUTF(organisation.getHomePage().getName());
            dos.writeUTF(nullWriter.apply(organisation.getHomePage().getUrl()));

            forEachWrite(organisation.getPeriods(), dos, period -> {
                dos.writeUTF(period.getStart().toString());
                dos.writeUTF(period.getEnd().toString());
                dos.writeUTF(period.getText());
                dos.writeUTF(nullWriter.apply(period.getOptionalText()));
            });
        });
    }

    private List<TimePeriodOrganisation> readTimePeriod(DataInputStream dis) throws IOException {
        return forListRead(dis, organisations -> {
            String name = dis.readUTF();
            String url = dis.readUTF();
            url = url.equals("") ? null : url;

            organisations.add(new TimePeriodOrganisation(
                    new Contact(name, url),
                    forListRead(dis, periods -> {
                        String optionalText;
                        periods.add(new TimePeriodOrganisation.TimePeriod(
                                LocalDate.parse(dis.readUTF()),
                                LocalDate.parse(dis.readUTF()),
                                dis.readUTF(),
                                (optionalText = dis.readUTF()).equals("") ? null : optionalText));
                    })
            ));
        });
    }

    private <E> void forEachWrite(Collection<? extends E> collection, DataOutputStream dos, IOConsumer<? super E> action) throws IOException {
        Objects.requireNonNull(action);
        dos.writeInt(collection.size());
        for (E e : collection) {
            action.accept(e);
        }
    }

    private void forRead(DataInputStream dis, IORunnable action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.run();
        }
    }

    private <E> List<E> forListRead(DataInputStream dis, IOConsumer<List<E>> action) throws IOException {
        List<E> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.accept(list);
        }
        return list;
    }
}

