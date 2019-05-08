package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public interface SerializationStrategy {

    void setStorageRealisation(AbstractFilePathStorage storageRealisation);

    void doDelete(Object filePath);

    void doSave(Resume resume, Object filePath);

    Resume doGet(Object filePath);

    Object getSearchKey(String uuid);

    List<Resume> doCopyAll();

    void doUpdate(Object filePath, Resume resume);

    boolean checkForExistence(Object filePath);

    void clear();

    int size();
}
