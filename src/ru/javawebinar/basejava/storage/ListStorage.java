package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> storageClone = storage.subList(0, storage.size());
        Collections.sort(storageClone);
        return storageClone;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void removeBySearchKey(Object index) {
        storage.remove(((Integer) index).intValue());
    }

    @Override
    protected void storeBySearchKey(Resume r, Object index) {
        storage.add(r);
    }

    @Override
    protected Resume getResumeBySearchKey(Object searchKey) {
        return storage.get((Integer) searchKey);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        int index = 0;
        for (Resume r : storage) {
            if (r.getUuid().equals(uuid)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    protected void replaceInSearchKey(Object index, Resume r) {
        storage.set((Integer) index, r);
    }

    @Override
    protected boolean checkForExistence(Object index) {
        return (Integer) index >= 0;
    }
}
