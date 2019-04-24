package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class MapUuidStorage extends AbstractMapStorage {

    @Override
    protected void removeBySearchKey(Object searchKey) {
        getStorage().remove(searchKey);
    }

    @Override
    protected Resume getResumeBySearchKey(Object searchKey) {
        return getStorage().get(searchKey);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void replaceInSearchKey(Object searchKey, Resume resume) {
        getStorage().replace((String) searchKey, resume);
    }

    @Override
    protected boolean checkForExistence(Object searchKey) {
        return getStorage().containsKey(searchKey);
    }
}
