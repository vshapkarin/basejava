package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume r) {
        Object searchKey = getSearchKey(r.getUuid());
        if (!checkForExistence(searchKey)) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            replaceInSearchKey(searchKey, r);
        }
    }

    @Override
    public void save(Resume r) {
        Object searchKey = getSearchKey(r.getUuid());
        if (checkForExistence(searchKey)) {
            throw new ExistStorageException(r.getUuid());
        } else {
            storeBySearchKey(r, searchKey);
        }
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!checkForExistence(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return getResumeBySearchKey(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!checkForExistence(searchKey)) {
            throw new NotExistStorageException(uuid);
        } else {
            removeBySearchKey(searchKey);
        }
    }

    protected abstract void removeBySearchKey(Object searchKey);

    protected abstract void storeBySearchKey(Resume r, Object searchKey);

    protected abstract Resume getResumeBySearchKey(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract void replaceInSearchKey(Object searchKey, Resume r);

    protected abstract boolean checkForExistence(Object searchKey);
}
