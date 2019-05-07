package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public class Context<T> extends AbstractStorage<T> {
    private SerializationStrategy<T> strategy;

    public Context(SerializationStrategy<T> strategy) {
        this.strategy = strategy;
    }

    protected void doDelete(T filePath) {
        strategy.doDelete(filePath);
    }

    protected void doSave(Resume resume, T filePath) {
        strategy.doSave(resume, filePath);
    }

    protected Resume doGet(T filePath) {
        return strategy.doGet(filePath);
    }

    protected T getSearchKey(String uuid) {
        return strategy.getSearchKey(uuid);
    }

    protected List<Resume> doCopyAll() {
        return strategy.doCopyAll();
    }

    protected void doUpdate(T filePath, Resume resume) {
        strategy.doUpdate(filePath, resume);
    }

    protected boolean checkForExistence(T filePath) {
        return strategy.checkForExistence(filePath);
    }

    public void clear() {
        strategy.clear();
    }

    public int size() {
        return strategy.size();
    }
}
