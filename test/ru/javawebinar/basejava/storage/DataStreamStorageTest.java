package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.DataStreamSerializer;

public class DataStreamStorageTest extends AbstractStorageTest {

    public DataStreamStorageTest() {
        super(new PathStorage(new DataStreamSerializer(), STORAGE_DIR));
    }
}
