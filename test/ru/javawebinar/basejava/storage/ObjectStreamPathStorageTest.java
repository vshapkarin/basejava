package ru.javawebinar.basejava.storage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new ObjectStreamStorage().setStrategy(new PathStorage(STORAGE_DIR)));
    }
}
