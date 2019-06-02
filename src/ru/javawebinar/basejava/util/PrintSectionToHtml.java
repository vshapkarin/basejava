package ru.javawebinar.basejava.util;

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
            output.append("<tr><td colspan = \"2\"><a href=\"" + organisation.getHomePage().getUrl() + "\">"
                    + organisation.getHomePage().getName()
                    + "</a></tr></td>");
            for (TimePeriodOrganisation.TimePeriod period : organisation.getPeriods()) {
                String optionalText = period.getOptionalText();
                output.append("<tr><td width = \"20%\" style=\"vertical-align: top\">"
                        + datePrinter.apply(period.getStart()) + " - "
                        + datePrinter.apply(period.getEnd()) + "</td>"
                        + "<td><b>" + period.getText() + "</b>"
                        + "<br>" + (optionalText == null ? "" : optionalText) + "</td></tr>");
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
