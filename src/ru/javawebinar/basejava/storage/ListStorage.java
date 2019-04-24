package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
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
    protected void storeBySearchKey(Resume resume, Object index) {
        storage.add(resume);
    }

    @Override
    protected Resume getResumeBySearchKey(Object searchKey) {
        return storage.get((Integer) searchKey);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected List<Resume> getList() {
        return storage.subList(0, storage.size());
    }

    @Override
    protected void replaceInSearchKey(Object index, Resume resume) {
        storage.set((Integer) index, resume);
    }

    @Override
    protected boolean checkForExistence(Object index) {
        return (Integer) index >= 0;
    }
}
