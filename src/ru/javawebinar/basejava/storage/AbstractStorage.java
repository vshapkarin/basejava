package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<T> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        T searchKey = getExistSearchKey(resume.getUuid());
        replaceInSearchKey(searchKey, resume);
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        T searchKey = getNotExistSearchKey(resume.getUuid());
        storeBySearchKey(resume, searchKey);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        T searchKey = getExistSearchKey(uuid);
        return getResumeBySearchKey(searchKey);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        T searchKey = getExistSearchKey(uuid);
        removeBySearchKey(searchKey);
    }

    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> list = getList();
        Collections.sort(list);
        return list;
    }

    private T getExistSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);
        if (!checkForExistence(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private T getNotExistSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);
        if (checkForExistence(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void removeBySearchKey(T searchKey);

    protected abstract void storeBySearchKey(Resume resume, T searchKey);

    protected abstract Resume getResumeBySearchKey(T searchKey);

    protected abstract T getSearchKey(String uuid);

    protected abstract List<Resume> getList();

    protected abstract void replaceInSearchKey(T searchKey, Resume resume);

    protected abstract boolean checkForExistence(T searchKey);
}
