package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface SerializationStrategy<T> {

    void doDelete(T filePath);

    void doSave(Resume resume, T filePath);

    Resume doGet(T filePath);

    T getSearchKey(String uuid);

    List<Resume> doCopyAll();

    void doUpdate(T filePath, Resume resume);

    boolean checkForExistence(T filePath);

    void clear();

    int size();

    void doWrite(Resume r, OutputStream os) throws IOException;

    Resume doRead(InputStream is) throws IOException;
}
