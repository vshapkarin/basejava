import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private static final int ARRAY_LENGTH = 10000;
    private Resume[] storage = new Resume[ARRAY_LENGTH];
    private int size;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    private int contains(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    void update(Resume resume) {
        int index = this.contains(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
        } else {
            System.out.println("Resume doesn't exist!");
        }
    }

    void save(Resume resume) {
        if (size == ARRAY_LENGTH) {
            System.out.println("Storage is already full!");
            return;
        }

        if (this.contains(resume.getUuid()) == -1) {
            storage[size] = resume;
            size++;
        } else {
            System.out.println("Resume already exists!");
        }
    }

    Resume get(String uuid) {
        int index = this.contains(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            System.out.println("Resume doesn't exist!");
            return null;
        }
    }

    void delete(String uuid) {
        int index = this.contains(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Resume doesn't exist!");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return size;
    }
}
