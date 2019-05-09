package ru.javawebinar.basejava.storage;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {

    public ObjectStreamFileStorageTest() {
        super(new FileStorage(new ObjectStreamStorage(), STORAGE_DIR));
    }
}