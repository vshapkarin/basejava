package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        traversal(new File("."));
    }

    private static void traversal(File directory) {
        File[] filesInDirectory = directory.listFiles();
        for (File currentFile : filesInDirectory) {
            if (currentFile.isDirectory()) {
                traversal(currentFile);
            } else {
                System.out.println(currentFile);
            }
        }
    }
}
