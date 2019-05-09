package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PathStorage extends AbstractSerializationStorage {
    private Path directory;
    private SerializationStrategy storageRealisation;

    protected PathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void setStorageRealisation(SerializationStrategy storageRealisation) {
        this.storageRealisation = storageRealisation;
    }

    @Override
    public void doDelete(Object path) {
        try {
            Files.delete((Path) path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", ((Path) path).getFileName().toString(), e);
        }
    }

    @Override
    public void doSave(Resume resume, Object path) {
        try {
            Files.createFile((Path) path);
        } catch (IOException e) {
            throw new StorageException("Can't create new path in " + ((Path) path).toAbsolutePath(), ((Path) path).getFileName().toString(), e);
        }
        doUpdate(path, resume);
    }

    @Override
    public Resume doGet(Object path) {
        try {
            return storageRealisation.doRead(new BufferedInputStream(Files.newInputStream((Path) path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", ((Path) path).getFileName().toString(), e);
        }
    }

    @Override
    public Path getSearchKey(String uuid) {
        return Paths.get(directory + "\\" + uuid);
    }

    @Override
    public List<Resume> doCopyAll() {
        List<Resume> list = new ArrayList<>();
        try {
            Files.list(directory).forEach(x -> list.add(doGet(x)));
        } catch (IOException e) {
            throw new StorageException("Directory opening error", directory.getFileName().toString(), e);
        }
        return list;
    }

    @Override
    public void doUpdate(Object path, Resume resume) {
        try {
            storageRealisation.doWrite(resume, new BufferedOutputStream(Files.newOutputStream((Path) path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", resume.getUuid(), e);
        }
    }

    @Override
    public boolean checkForExistence(Object path) {
        return Files.exists((Path) path);
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path clear error", directory.getFileName().toString(), e);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Path counting error", directory.getFileName().toString(), e);
        }
    }
}