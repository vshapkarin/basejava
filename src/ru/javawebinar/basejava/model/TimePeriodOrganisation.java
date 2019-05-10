package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.DateUtil;
import ru.javawebinar.basejava.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class TimePeriodOrganisation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Contact homePage;
    private List<TimePeriod> periods;

    public TimePeriodOrganisation() {
    }

    public TimePeriodOrganisation(String name, String url, TimePeriod... periods) {
        this(new Contact(name, url), Arrays.asList(periods));
    }

    public TimePeriodOrganisation(Contact homePage, List<TimePeriod> periods) {
        Objects.requireNonNull(homePage, "organisation name must not be null");
        Objects.requireNonNull(periods, "time periods must not be null");
        this.homePage = homePage;
        this.periods = periods;
    }

    public Contact getHomePage() {
        return homePage;
    }

    public List<TimePeriod> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimePeriodOrganisation that = (TimePeriodOrganisation) o;

        if (!homePage.equals(that.homePage)) return false;
        return periods.equals(that.periods);

    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organisation - " + homePage + '(' + periods + ')';
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TimePeriod implements Serializable{
        private static final long serialVersionUID = 1L;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate start;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate end;
        private String text;
        private String optionalText;

        public TimePeriod() {
        }

        public TimePeriod(int startYear, Month startMonth, String text, String optionalText) {
            this(DateUtil.of(startYear, startMonth), DateUtil.NOW, text, optionalText);
        }

        public TimePeriod(int startYear, Month startMonth, int endYear, Month endMonth, String text, String optionalText) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth), text, optionalText);
        }

        public TimePeriod(LocalDate start, LocalDate end, String text, String optionalText) {
            Objects.requireNonNull(start, "start date must not be null");
            Objects.requireNonNull(end, "end date must not be null");
            Objects.requireNonNull(text, "titul text must not be null");
            this.start = start;
            this.end = end;
            this.text = text;
            this.optionalText = optionalText;
        }

        public LocalDate getStart() {
            return start;
        }

        public LocalDate getEnd() {
            return end;
        }

        public String getText() {
            return text;
        }

        public String getOptionalText() {
            return optionalText;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TimePeriod period = (TimePeriod) o;

            if (!start.equals(period.start)) return false;
            if (!end.equals(period.end)) return false;
            if (!text.equals(period.text)) return false;
            return optionalText != null ? optionalText.equals(period.optionalText) : period.optionalText == null;
        }

        @Override
        public int hashCode() {
            int result = start.hashCode();
            result = 31 * result + end.hashCode();
            result = 31 * result + text.hashCode();
            result = 31 * result + (optionalText != null ? optionalText.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "From " + start +
                    " to " + end +
                    ", '" + text + '\'' +
                    ", '" + optionalText + '\'';
        }
    }
}
