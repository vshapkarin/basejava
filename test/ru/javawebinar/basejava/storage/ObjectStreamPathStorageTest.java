package ru.javawebinar.basejava.storage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new Context<>(new PathStorage(STORAGE_DIR.toString())));
    }
}
