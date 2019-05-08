package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public abstract class AbstractFilePathStorage extends AbstractStorage<Object> {
    private SerializationStrategy strategy;

    public void setStrategy(SerializationStrategy strategy, AbstractFilePathStorage storageRealisation) {
        this.strategy = strategy;
        this.strategy.setStorageRealisation(storageRealisation);
    }

    protected void doDelete(Object filePath) {
        strategy.doDelete(filePath);
    }

    protected void doSave(Resume resume, Object filePath) {
        strategy.doSave(resume, filePath);
    }

    protected Resume doGet(Object filePath) {
        return strategy.doGet(filePath);
    }

    protected Object getSearchKey(String uuid) {
        return strategy.getSearchKey(uuid);
    }

    protected List<Resume> doCopyAll() {
        return strategy.doCopyAll();
    }

    protected void doUpdate(Object filePath, Resume resume) {
        strategy.doUpdate(filePath, resume);
    }

    protected boolean checkForExistence(Object filePath) {
        return strategy.checkForExistence(filePath);
    }

    public void clear() {
        strategy.clear();
    }

    public int size() {
        return strategy.size();
    }

    public abstract void doWrite(Resume resume, OutputStream os) throws IOException;

    public abstract Resume doRead(InputStream is) throws IOException;
}
