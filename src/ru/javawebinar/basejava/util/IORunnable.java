package ru.javawebinar.basejava.util;

import java.io.IOException;

@FunctionalInterface
public interface IORunnable {
    void run() throws IOException;
}
