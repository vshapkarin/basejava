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
        doUpdate(searchKey, resume);
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        T searchKey = getNotExistSearchKey(resume.getUuid());
        doSave(resume, searchKey);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        T searchKey = getExistSearchKey(uuid);
        return doGet(searchKey);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        T searchKey = getExistSearchKey(uuid);
        doDelete(searchKey);
    }

    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> list = doCopyAll();
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

    protected abstract void doDelete(T searchKey);

    protected abstract void doSave(Resume resume, T searchKey);

    protected abstract Resume doGet(T searchKey);

    protected abstract T getSearchKey(String uuid);

    protected abstract List<Resume> doCopyAll();

    protected abstract void doUpdate(T searchKey, Resume resume);

    protected abstract boolean checkForExistence(T searchKey);
}
