package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        bypassDir(new File("."));
    }

    private static void bypassDir(File directory) {
        File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory == null) {
            return;
        }
        for (File currentFile : filesInDirectory) {
            if (currentFile.isDirectory()) {
                bypassDir(currentFile);
            } else {
                System.out.println(currentFile.getName());
            }
        }
    }
}
