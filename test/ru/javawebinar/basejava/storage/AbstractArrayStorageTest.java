package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorageTest {
    private Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    public AbstractArrayStorageTest(Class storage) throws IllegalAccessException, InstantiationException {
        this.storage = (Storage) storage.newInstance();
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        Resume r = new Resume(UUID_1);
        storage.update(r);
    }

    @Test
    public void getAll() throws Exception {
        Resume[] reflectionStorage = (Resume[]) AbstractArrayStorage.class.getDeclaredField("storage").get(storage);
        reflectionStorage = Arrays.copyOf(reflectionStorage, storage.size());
        Assert.assertArrayEquals(reflectionStorage, storage.getAll());
    }

    @Test(expected = StorageException.class)
    public void save() {
        try {
            for (int i = 0; i < 10000 - 3; i++) {
                storage.save(new Resume("newUuid" + i ));
            }
        } catch (Exception c) {
            Assert.fail();
        }

        Assert.assertEquals(10000, storage.size());
        storage.save(new Resume(""));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete("dummy");
        storage.delete("uuid1");
        storage.delete("uuid2");
        storage.delete("uuid3");
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void get() throws Exception {
        Resume[] reflectionStorage = (Resume[]) AbstractArrayStorage.class.getDeclaredField("storage").get(storage);
        Assert.assertEquals(reflectionStorage[0], storage.get(UUID_1));
        Assert.assertEquals(reflectionStorage[1], storage.get(UUID_2));
        Assert.assertEquals(reflectionStorage[2], storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }
}