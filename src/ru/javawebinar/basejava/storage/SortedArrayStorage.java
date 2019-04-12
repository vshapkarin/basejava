package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            System.out.println("Resume " + r.getUuid() + " not exist");
            return;
        }
        shiftTo(index);
        store(r);
    }

    @Override
    public void save(Resume r) {
        if (getIndex(r.getUuid()) >= 0) {
            System.out.println("Resume " + r.getUuid() + " already exist");
        } else if (size >= STORAGE_LIMIT) {
            System.out.println("Storage overflow");
        } else {
            store(r);
            size++;
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    private void store(Resume r){
        int insertionPoint = (-1) * (Arrays.binarySearch(storage, 0, size, r)) - 1;

        for (int i = size; i > insertionPoint; i--) {
            storage[i] = storage[i - 1];
        }
        storage[insertionPoint] = r;
    }
}