package ru.javawebinar.basejava.storage;

public class ObjectStreamStorageTest extends AbstractStorageTest {

    public ObjectStreamStorageTest() {
        super(new ObjectStreamStorage().setStrategy(new FileStorage(STORAGE_DIR)));
    }
}