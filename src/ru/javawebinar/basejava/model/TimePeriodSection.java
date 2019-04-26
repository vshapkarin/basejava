package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimePeriodSection extends Section {
    private List<TimePeriod> content;

    public TimePeriodSection (TimePeriod... content) {
        this.content = Arrays.asList(content);
    }

    public List<TimePeriod> getContent() {
        return new ArrayList<>(content);
    }

    public void setContent(List<TimePeriod> content) {
        this.content = content;
    }


}
