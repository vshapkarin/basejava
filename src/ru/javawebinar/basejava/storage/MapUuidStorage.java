package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> storageValues = new ArrayList<>(storage.values());
        Collections.sort(storageValues);
        return storageValues;
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
    protected Object getSearchKey(String searchKey) {
        return searchKey;
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
