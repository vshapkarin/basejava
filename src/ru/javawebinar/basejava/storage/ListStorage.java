package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.toArray(resumes);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void removeBySearchKey(Object searchKey) {
        int index = (Integer) searchKey;
        storage.remove(index);
    }

    @Override
    protected void storeBySearchKey(Resume r, Object searchKey) {
        storage.add(r);
    }

    @Override
    protected Resume getResumeBySearchKey(Object searchKey) {
        int index = (Integer) searchKey;
        return storage.get(index);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        int index = 0;
        for (Resume r : storage) {
            if (r.getUuid() == uuid) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    protected void replaceInSearchKey(Object searchKey, Resume r) {
        int index = (Integer) searchKey;
        storage.set(index, r);
    }

    @Override
    protected boolean checkForExistence(Object searchKey) {
        return (Integer) searchKey >= 0;
    }
}
