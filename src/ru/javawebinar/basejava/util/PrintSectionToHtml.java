package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.TextListSection;
import ru.javawebinar.basejava.model.TimePeriodOrganisation;
import ru.javawebinar.basejava.model.TimePeriodSection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrintSectionToHtml {
    public static String organisationsToHtml(TimePeriodSection section) {
        Function<LocalDate, String> datePrinter = date -> date.getYear() > 2999 ? "Сейчас" : date.format(DateTimeFormatter.ofPattern("MM/YYYY"));
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

    public static String listToHtml(TextListSection section) {
        return "<ul>" + section.getContent().stream().map(a -> "<li>" + a).collect(Collectors.joining("\n")) + "</ul>";
    }

    public static String listToString(TextListSection section) {
        return String.join("\n", section.getContent());
    }
}
