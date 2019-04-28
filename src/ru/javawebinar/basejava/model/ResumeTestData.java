package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("1488", "Григорий Кислин");
        Map<ContactType, Contact> contacts = new EnumMap<>(ContactType.class);
        contacts.put(ContactType.TELEPHONE, new Contact("+7(921) 855-0482"));
        contacts.put(ContactType.SKYPE, new Contact("grigory.kislin"));
        contacts.put(ContactType.EMAIL, new Contact("gkislin@yandex.ru"));
        contacts.put(ContactType.LINKEDIN, new Contact(""));
        contacts.put(ContactType.GITHUB, new Contact(""));
        contacts.put(ContactType.STACKOVERFLOW, new Contact(""));
        contacts.put(ContactType.HOMEPAGE, new Contact(""));
        resume.setContacts(contacts);

        Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);
        sections.put(SectionType.PERSONAL, new TextOnlySection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        sections.put(SectionType.OBJECTIVE, new TextOnlySection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        sections.put(SectionType.ACHIEVEMENT, new TextListSection("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.",
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.",
                "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.",
                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.",
                "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).",
                "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа."));
        sections.put(SectionType.QUALIFICATIONS, new TextListSection("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce",
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,",
                "MySQL, SQLite, MS SQL, HSQLDB",
                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy,",
                "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts,",
                "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).",
                "Python: Django.",
                "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js",
                "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka",
                "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.",
                "Инструменты: Maven + plugin development, Gradle, настройка Ngnix,",
                "администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer.",
                "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования",
                "Родной русский, английский \"upper intermediate\""));
        sections.put(SectionType.EXPERIENCE, new TimePeriodSection(new TimePeriod("Java Online Projects",
                null,
                LocalDate.of(2013, 10, 1),
                LocalDate.now(),
                "Автор проекта",
                "Создание, организация и проведение Java онлайн проектов и стажировок."),
                new TimePeriod("Wrike",
                        null,
                        LocalDate.of(2014, 10, 1),
                        LocalDate.of(2016, 1, 1),
                        "Старший разработчик (backend)",
                        "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."),
                new TimePeriod("RIT Center",
                        null,
                        LocalDate.of(2012, 4, 1),
                        LocalDate.of(2014, 10, 1),
                        "Java архитектор",
                        "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python")
        ));
        sections.put(SectionType.EDUCATION, new TimePeriodSection(new TimePeriod("Coursera",
                "coursera.com",
                LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 5, 1),
                "\"Functional Programming Principles in Scala\" by Martin Odersky"),
                new TimePeriod("Luxoft",
                        null,
                        LocalDate.of(2011, 3, 1),
                        LocalDate.of(2011, 4, 1),
                        "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\""),
                new TimePeriod("Siemens AG",
                        null,
                        LocalDate.of(2005, 1, 1),
                        LocalDate.of(2005, 4, 1),
                        "3 месяца обучения мобильным IN сетям (Берлин)")

        ));
        resume.setSections(sections);

        System.out.printf("%s\n\n", resume.getFullName());

        for (Map.Entry<ContactType, Contact> entry : resume.getContacts().entrySet()) {
            System.out.println(entry.getKey().getTitle() + " " + entry.getValue());
        }

        System.out.println();

        for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
            switch (entry.getKey()) {
                case PERSONAL:
                    System.out.printf("%s:\n%s\n\n", SectionType.PERSONAL.getTitle(), entry.getValue());
                    break;
                case OBJECTIVE:
                    System.out.printf("%s:\n%s\n\n", SectionType.OBJECTIVE.getTitle(), entry.getValue());
                    break;
                case ACHIEVEMENT:
                    StringBuilder builder = new StringBuilder().append(SectionType.ACHIEVEMENT.getTitle()).append(":\n");
                    TextListSection listSection = (TextListSection) entry.getValue();
                    for (String string : listSection.getContent()) {
                        builder.append('-').append(string).append('\n');
                    }
                    System.out.println(builder.toString());
                    break;
                case QUALIFICATIONS:
                    StringBuilder builder2 = new StringBuilder().append(SectionType.QUALIFICATIONS.getTitle()).append(":\n");
                    TextListSection listSection2 = (TextListSection) entry.getValue();
                    for (String string : listSection2.getContent()) {
                        builder2.append('-').append(string).append('\n');
                    }
                    System.out.println(builder2.toString());
                    break;
                case EXPERIENCE:
                    System.out.println(SectionType.EXPERIENCE.getTitle() + ":");
                    TimePeriodSection periodSection = (TimePeriodSection) entry.getValue();
                    for (TimePeriod period : periodSection.getContent()) {
                        System.out.printf("%s - %d/%d - %d/%d\n%s\n%s\n", period.getHomePage(),
                                period.getStart().getYear(),
                                period.getStart().getMonthValue(),
                                period.getEnd().getYear(),
                                period.getEnd().getMonthValue(),
                                period.getText(),
                                period.getOptionalText());
                    }
                    System.out.println();
                    break;
                case EDUCATION:
                    System.out.println(SectionType.EDUCATION.getTitle() + ":");
                    TimePeriodSection periodSection2 = (TimePeriodSection) entry.getValue();
                    for (TimePeriod period : periodSection2.getContent()) {
                        System.out.printf("%s - %d/%d - %d/%d\n%s\n", period.getHomePage(),
                                period.getStart().getYear(),
                                period.getStart().getMonthValue(),
                                period.getEnd().getYear(),
                                period.getEnd().getMonthValue(),
                                period.getText());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
