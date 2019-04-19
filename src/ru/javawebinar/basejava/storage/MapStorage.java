package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.values().toArray(resumes);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void removeBySearchKey(Object searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected void storeBySearchKey(Resume r, Object searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume getResumeBySearchKey(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void replaceInSearchKey(Object searchKey, Resume r) {
        storage.replace((String) searchKey, r);
    }

    @Override
    protected boolean checkForExistence(Object searchKey) {
        return storage.containsKey(searchKey);
    }
}
