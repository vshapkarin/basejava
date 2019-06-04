package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.TextListSection;
import ru.javawebinar.basejava.model.TimePeriodOrganisation;
import ru.javawebinar.basejava.model.TimePeriodSection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrintSectionToHtml {
    private static Function<LocalDate, String> datePrinter = date -> date.getYear() > 2999 ? "Сейчас" : date.format(DateTimeFormatter.ofPattern("MM/YYYY"));

    public static String organisationsToHtml(TimePeriodSection section) {
        StringBuilder output = new StringBuilder().append("<table>");
        for (TimePeriodOrganisation organisation : section.getContent()) {
            Contact homePage = organisation.getHomePage();
            if (homePage.getUrl() == null) {
                output.append("<tr><td colspan = \"2\"><b>")
                        .append(homePage.getName())
                        .append("</b></tr></td>");
            } else {
                output.append("<tr><td colspan = \"2\"><a href=\"")
                        .append(homePage.getUrl())
                        .append("\"><b>")
                        .append(homePage.getName())
                        .append("</b></a></tr></td>");
            }
            for (TimePeriodOrganisation.TimePeriod period : organisation.getPeriods()) {
                String optionalText = period.getOptionalText();
                output.append("<tr><td width = \"20%\" style=\"vertical-align: top\">")
                        .append(datePrinter.apply(period.getStart()))
                        .append(" - ")
                        .append(datePrinter.apply(period.getEnd())).append("</td>")
                        .append("<td><b>")
                        .append(period.getText()).append("</b>").append("<br>")
                        .append(optionalText == null ? "" : optionalText).append("</td></tr>");
            }
        }
        output.append("</table>");
        return output.toString();
    }

    public static String organisationsToString(TimePeriodSection section) {
        StringBuilder output = new StringBuilder();
        for (TimePeriodOrganisation organisation : section.getContent()) {
            Contact homePage = organisation.getHomePage();
            output.append(TimePeriodColumnType.ORGANISATION.getTitle())
                    .append(homePage.getName()).append('\n')
                    .append(TimePeriodColumnType.URL.getTitle())
                    .append(homePage.getUrl()).append('\n');
            for (TimePeriodOrganisation.TimePeriod period : organisation.getPeriods()) {
                output.append(TimePeriodColumnType.START_DATE.getTitle())
                        .append(period.getStart().toString()).append('\n')
                        .append(TimePeriodColumnType.END_DATE.getTitle())
                        .append(period.getEnd().toString()).append('\n')
                        .append(TimePeriodColumnType.TEXT.getTitle())
                        .append(period.getText()).append('\n')
                        .append(TimePeriodColumnType.OPTIONAL_TEXT.getTitle())
                        .append(period.getOptionalText()).append('\n');
            }
            output.append('\n');
        }
        return output.toString();
    }

    public static String listToHtml(TextListSection section) {
        return "<ul>" + section.getContent().stream().map(a -> "<li>" + a).collect(Collectors.joining("\n")) + "</ul>";
    }

    public static String listToString(TextListSection section) {
        return String.join("\n", section.getContent());
    }

    public static List<TimePeriodOrganisation.TimePeriod> parsePeriods(Scanner scanner) {
        List<TimePeriodOrganisation.TimePeriod> periods = new ArrayList<>();
        String column;
        while (scanner.hasNextLine() && (column = scanner.nextLine()).startsWith(TimePeriodColumnType.START_DATE.getTitle())) {
            periods.add(new TimePeriodOrganisation.TimePeriod(LocalDate.parse(column.substring(TimePeriodColumnType.START_DATE.getTitleLength())),
                    LocalDate.parse(scanner.nextLine().substring(TimePeriodColumnType.END_DATE.getTitleLength())),
                    scanner.nextLine().substring(TimePeriodColumnType.TEXT.getTitleLength()),
                    (column = scanner.nextLine().substring(TimePeriodColumnType.OPTIONAL_TEXT.getTitleLength())).equals("null") ? null : column));
        }
        return periods;
    }
}
