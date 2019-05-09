package ru.javawebinar.basejava.storage;

public abstract class AbstractSerializationStorage extends AbstractStorage<Object> {
    private SerializationStrategy storageRealisation;

    public void setStorageRealisation(SerializationStrategy storageRealisation) {
        this.storageRealisation = storageRealisation;
    }
}
