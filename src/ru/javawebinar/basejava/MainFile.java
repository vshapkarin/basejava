package ru.javawebinar.basejava;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainFile {
    private static int depthCounter = 0;

    public static void main(String[] args) {
        bypassDir(new File("."));
    }

    private static void bypassDir(File directory) {
        File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory != null) {
            for (File currentFile : filesInDirectory) {
                if (currentFile.isDirectory()) {
                    depthCounter++;
                    printFile(currentFile, ">");
                    bypassDir(currentFile);
                } else {
                    printFile(currentFile, "|---");
                }
            }
            depthCounter--;
        }
    }

    private static void printFile(File currentFile, String identifier) {
        System.out.println(IntStream.range(0, depthCounter)
                .mapToObj(i -> "   ")
                .collect(Collectors.joining(""))
                + identifier
                + currentFile.getName());
    }
}
