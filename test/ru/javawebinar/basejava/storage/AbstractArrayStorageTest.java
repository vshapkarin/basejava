package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected AbstractArrayStorageTest (Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveOverFlow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume("newUuid" + i));
            }
        } catch (StorageException c) {
            Assert.fail("Error filling array");
        }
        Assert.assertEquals(AbstractArrayStorage.STORAGE_LIMIT, storage.size());
        storage.save(new Resume(""));
    }
}
