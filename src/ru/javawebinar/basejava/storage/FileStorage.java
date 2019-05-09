package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<Object> {
    private File directory;
    private SerializationStrategy storageRealisation;

    protected FileStorage(SerializationStrategy storageRealisation, String dir) {
        this.storageRealisation = storageRealisation;
        File directory = new File(dir);
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
    public void doDelete(Object file) {
        if (!((File) file).delete()) {
            throw new StorageException("Can't delete file", ((File) file).getName());
        }
    }

    @Override
    public void doSave(Resume resume, Object file) {
        try {
            ((File) file).createNewFile();
        } catch (IOException e) {
            throw new StorageException("Can't create new file in " + ((File) file).getAbsolutePath(), ((File) file).getName(), e);
        }
        doUpdate(file, resume);
    }

    @Override
    public Resume doGet(Object file) {
        try {
            return storageRealisation.doRead(new BufferedInputStream(new FileInputStream((File) file)));
        } catch (IOException e) {
            throw new StorageException("IO error", ((File) file).getName(), e);
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
    public void doUpdate(Object file, Resume resume) {
        try {
            storageRealisation.doWrite(resume, new BufferedOutputStream(new FileOutputStream((File) file)));
        } catch (IOException e) {
            throw new StorageException("File update error in " + ((File) file).getAbsolutePath(), ((File) file).getName(), e);
        }
    }

    @Override
    public boolean checkForExistence(Object file) {
        return ((File) file).exists();
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
}
