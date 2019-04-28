package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TimePeriodOrganisation {
    private Link homePage;
    private List<TimePeriod> periods;

    public TimePeriodOrganisation(String name, String url, TimePeriod... periods) {
        Objects.requireNonNull(name, "organisation name must not be null");
        Objects.requireNonNull(periods, "time periods must not be null");
        this.homePage = new Link(name, url);
        this.periods = Arrays.asList(periods);
    }

    public Link getHomePage() {
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
}
