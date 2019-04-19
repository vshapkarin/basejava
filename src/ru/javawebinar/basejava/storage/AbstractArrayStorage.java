package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

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
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected void removeBySearchKey(Object searchKey) {
        int index = (Integer) searchKey;
        removeFromArray(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void storeBySearchKey(Resume r, Object searchKey) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            int index = (Integer) searchKey;
            storeInArray(r, index);
            size++;
        }
    }

    @Override
    protected Resume getResumeBySearchKey(Object searchKey)
    {
        int index = (Integer) searchKey;
        return storage[index];
    }

    @Override
    protected void replaceInSearchKey(Object searchKey, Resume r) {
        int index = (Integer) searchKey;
        storage[index] = r;
    }

    @Override
    protected boolean checkForExistence(Object searchKey) {
        return (Integer) searchKey >= 0;
    }

    protected abstract void storeInArray(Resume r, int index);

    protected abstract void removeFromArray(int index);
}
