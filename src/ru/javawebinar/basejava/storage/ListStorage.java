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
    protected void removeByIndex(int index) {
        storage.remove(index);
    }

    @Override
    protected void storeByIndex(Resume r, int index) {
        storage.add(r);
    }

    @Override
    protected Resume getResumeByIndex(int index) {
        return storage.get(index);
    }

    @Override
    protected int getIndex(String uuid) {
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
    protected void replaceInIndex(int index, Resume r) {
        storage.set(index, r);
    }
}
