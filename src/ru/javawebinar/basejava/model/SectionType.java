package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.PrintSectionToHtml;

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
            return getHeader(getTitle()) + PrintSectionToHtml.listToHtml((TextListSection) section);
        }
    },
    QUALIFICATIONS("Квалификация") {
        @Override
        public String toHtml0(AbstractSection section) {
            return getHeader(getTitle()) + PrintSectionToHtml.listToHtml((TextListSection) section);
        }
    },
    EXPERIENCE("Опыт работы") {
        @Override
        public String toHtml0(AbstractSection section) {
            return getHeader(getTitle()) + PrintSectionToHtml.organisationsToHtml((TimePeriodSection) section);
        }
    },
    EDUCATION("Образование") {
        @Override
        public String toHtml0(AbstractSection section) {
            return getHeader(getTitle()) + PrintSectionToHtml.organisationsToHtml((TimePeriodSection) section);
        }
    };

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
}
