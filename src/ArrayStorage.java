import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; storage[i] != null; i++) {
            storage[i] = null;
        }
    }

    void save(Resume r) {
        storage[this.size()] = r;
    }

    Resume get(String uuid) {
        int index = 0;
        while (storage[index] != null) {
            if (uuid.compareTo(storage[index].uuid) == 0) {
                break;
            } else {
                index++;
            }
        }
        return storage[index];
    }

    void delete(String uuid) {
        for (int i = 0; storage[i] != null; i++) {
            if (storage[i].uuid.compareTo(uuid) == 0) {
                for (; storage[i] != null && i < storage.length - 1; i++) {
                    storage[i] = storage[i + 1];
                }
                storage[storage.length - 1] = null;          //If the array was full
                break;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, this.size());
    }

    int size() {
        int index = 0;
        while (index < storage.length) {
            if (storage[index] == null) {
                break;
            } else {
                index++;
            }
        }
        return index;
    }
}
