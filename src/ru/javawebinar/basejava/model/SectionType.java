package ru.javawebinar.basejava.model;

import java.util.stream.Collectors;

public enum SectionType {
    PERSONAL("Личные качества") {
        @Override
        public String toHtml0(AbstractSection section) {
            return getHeader(getTitle()) + section.toString();
        }
    },
    OBJECTIVE("Позиция") {
        @Override
        public String toHtml0(AbstractSection section) {
            return getHeader(getTitle()) + section.toString();
        }
    },
    ACHIEVEMENT("Достижения") {
        @Override
        public String toHtml0(AbstractSection section) {
            return getHeader(getTitle()) + printListToHtml((TextListSection) section);
        }
    },
    QUALIFICATIONS("Квалификация") {
        @Override
        public String toHtml0(AbstractSection section) {
            return getHeader(getTitle()) + printListToHtml((TextListSection) section);
        }
    },
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    private String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected String toHtml0(AbstractSection section) {
        return title;
    }

    public String toHtml(AbstractSection section) {
        return (section == null) ? "" : toHtml0(section);
    }

    protected String getHeader(String title) {
        return "<h3>" + title + "</h3>\n";
    }

    public String printListToHtml(TextListSection section) {
       return "<ul>" + section.getContent().stream().map(a -> "<li>" + a).collect(Collectors.joining("\n")) + "</ul>";
    }

    public static String printList(TextListSection section) {
        return section.getContent().stream().collect(Collectors.joining("\n"));
    }
}
