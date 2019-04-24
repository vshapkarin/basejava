package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class MapResumeStorage extends AbstractMapStorage {

    @Override
    protected void removeBySearchKey(Object searchKey) {
        Resume key = (Resume) searchKey;
        getStorage().remove(key.getUuid());
    }

    @Override
    protected Resume getResumeBySearchKey(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return getStorage().get(uuid);
    }

    @Override
    protected void replaceInSearchKey(Object searchKey, Resume resume) {
        Resume key = (Resume) searchKey;
        getStorage().replace(key.getUuid(), resume);
    }

    @Override
    protected boolean checkForExistence(Object searchKey) {
        return searchKey != null;
    }
}
