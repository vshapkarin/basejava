package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        Object searchKey = getExistingKey(resume.getUuid());
        replaceInSearchKey(searchKey, resume);
    }

    @Override
    public void save(Resume resume) {
        Object searchKey = getNotExistingKey(resume.getUuid());
        storeBySearchKey(resume, searchKey);
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = getExistingKey(uuid);
        return getResumeBySearchKey(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getExistingKey(uuid);
        removeBySearchKey(searchKey);
    }

    public List<Resume> getAllSorted() {
        List<Resume> list = getList();
        Collections.sort(list);
        return list;
    }

    private Object getExistingKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!checkForExistence(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistingKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (checkForExistence(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void removeBySearchKey(Object searchKey);

    protected abstract void storeBySearchKey(Resume r, Object searchKey);

    protected abstract Resume getResumeBySearchKey(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract List<Resume> getList();

    protected abstract void replaceInSearchKey(Object searchKey, Resume r);

    protected abstract boolean checkForExistence(Object searchKey);
}
