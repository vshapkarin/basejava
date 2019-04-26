package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
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
    protected void removeBySearchKey(Integer index) {
        storage.remove(index.intValue());
    }

    @Override
    protected void storeBySearchKey(Resume resume, Integer index) {
        storage.add(resume);
    }

    @Override
    protected Resume getResumeBySearchKey(Integer searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected List<Resume> getList() {
        return new ArrayList<>(storage);
    }

    @Override
    protected void replaceInSearchKey(Integer index, Resume resume) {
        storage.set(index, resume);
    }

    @Override
    protected boolean checkForExistence(Integer index) {
        return index >= 0;
    }
}
