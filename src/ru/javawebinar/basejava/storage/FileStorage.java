package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> implements SerializationStrategy<File> {
    private File directory;

    protected FileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    public void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Can't delete file", file.getName());
        }
    }

    @Override
    public void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Can't create new file in " + file.getAbsolutePath(), file.getName(), e);
        }
        doUpdate(file, resume);
    }

    @Override
    public Resume doGet(File file) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    public File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    public List<Resume> doCopyAll() {
        File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory == null) {
            throw new StorageException("Directory read error in " + directory.getAbsolutePath(), null);
        }
        List<Resume> list = new ArrayList<>();
        for (File currentFile : filesInDirectory) {
            if (!currentFile.isDirectory()) {
                list.add(doGet(currentFile));
            }
        }
        return list;
    }

    @Override
    public void doUpdate(File file, Resume resume) {
        try {
            doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File update error in " + file.getAbsolutePath(), file.getName(), e);
        }
    }

    @Override
    public boolean checkForExistence(File file) {
        return file.exists();
    }

    @Override
    public void clear() {
        File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory != null) {
            for (File currentFile : filesInDirectory) {
                currentFile.delete();
            }
        }
    }

    @Override
    public int size() {
        String[] dirContent = directory.list();
        if (dirContent != null) {
            return dirContent.length;
        } else {
            return 0;
        }
    }

    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(resume);
        }
    }

    public Resume doRead(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}
