package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.TextListSection;

import java.util.stream.Collectors;

public class PrintListToHtml {
    public static String listToHtml(TextListSection section) {
        return "<ul>" + section.getContent().stream().map(a -> "<li>" + a).collect(Collectors.joining("\n")) + "</ul>";
    }

    public static String listToString(TextListSection section) {
        return String.join("\n", section.getContent());
    }
}
