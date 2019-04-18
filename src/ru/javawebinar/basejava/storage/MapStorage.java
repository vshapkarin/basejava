package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.values().toArray(resumes);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void removeByIndex(int index) {
        int counter = 0;
        Iterator entryIterator = storage.entrySet().iterator();
        while(entryIterator.hasNext()) {
            entryIterator.next();
            if (counter == index) {
                entryIterator.remove();
                return;
            }
            counter++;
        }
    }

    @Override
    protected void storeByIndex(Resume r, int index) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume getResumeByIndex(int index) {
        int counter = 0;
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (counter == index) {
                return entry.getValue();
            }
            counter++;
        }
        return null;
    }

    @Override
    protected int getIndex(String uuid) {
        int index = 0;
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (entry.getKey() == uuid) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    protected void replaceInIndex(int index, Resume r) {
        int counter = 0;
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (counter == index) {
                storage.replace(entry.getKey(), r);
                return;
            }
            counter++;
        }
    }
}
