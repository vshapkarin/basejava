package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = Arrays.asList(Arrays.copyOf(storage, size));
        Collections.sort(list);
        return list;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected void removeBySearchKey(Object index) {
        removeFromArray((Integer) index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void storeBySearchKey(Resume r, Object index) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            storeInArray(r, (Integer) index);
            size++;
        }
    }

    @Override
    protected Resume getResumeBySearchKey(Object index)
    {
        return storage[(Integer) index];
    }

    @Override
    protected void replaceInSearchKey(Object index, Resume r) {
        storage[(Integer) index] = r;
    }

    @Override
    protected boolean checkForExistence(Object index) {
        return (Integer) index >= 0;
    }

    protected abstract void storeInArray(Resume r, int index);

    protected abstract void removeFromArray(int index);
}
