package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
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
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Can't delete file", file.getName());
        }
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Can't create new file in " + file.getAbsolutePath(), file.getName(), e);
        }
        doUpdate(file, resume);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected List<Resume> doCopyAll() {
        File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory == null) {
            throw new StorageException("Directory read error in " + directory.getAbsolutePath(), null);
        }
        List<Resume> list = new ArrayList<>();
        for (File currentFile : filesInDirectory) {
            if (!currentFile.isDirectory()) {
                try {
                    list.add(doRead(currentFile));
                } catch (IOException e) {
                    throw new StorageException("IO error", currentFile.getName(), e);
                }
            }
        }
        return list;
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        try {
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("File update error in " + file.getAbsolutePath(), file.getName(), e);
        }
    }

    @Override
    protected boolean checkForExistence(File file) {
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

    protected abstract void doWrite(Resume resume, File file) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;
}
