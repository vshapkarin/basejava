package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage<String> {
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
    protected void storeBySearchKey(Resume resume, String searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected List<Resume> getList() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected void removeBySearchKey(String searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected Resume getResumeBySearchKey(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void replaceInSearchKey(String searchKey, Resume resume) {
        storage.replace(searchKey, resume);
    }

    @Override
    protected boolean checkForExistence(String  searchKey) {
        return storage.containsKey(searchKey);
    }
}
