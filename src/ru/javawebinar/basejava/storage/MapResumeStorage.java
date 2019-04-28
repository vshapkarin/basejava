package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void doSave(Resume resume, Resume searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected void doDelete(Resume searchKey) {
        storage.remove(searchKey.getUuid());
    }

    @Override
    protected Resume doGet(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doUpdate(Resume searchKey, Resume resume) {
        storage.replace(searchKey.getUuid(), resume);
    }

    @Override
    protected boolean checkForExistence(Resume searchKey) {
        return searchKey != null;
    }
}
