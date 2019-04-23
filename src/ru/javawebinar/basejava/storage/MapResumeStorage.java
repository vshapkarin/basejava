package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage {
    private Map<Resume, String> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> storageValues = new ArrayList<>(storage.keySet());
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
        storage.put(r, r.getFullName());
    }

    @Override
    protected Resume getResumeBySearchKey(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected Object getSearchKey(String searchKey) {
        for (Resume keys : storage.keySet()) {
            if (keys.getUuid().equals(searchKey)) {
                return keys;
            }
        }
        return null;
    }

    @Override
    protected void replaceInSearchKey(Object searchKey, Resume r) {
        removeBySearchKey(searchKey);
        storeBySearchKey(r, searchKey);
    }

    @Override
    protected boolean checkForExistence(Object searchKey) {
        return searchKey != null;
    }
}
