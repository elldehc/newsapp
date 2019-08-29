package com.java.liqibin.util;

import java.time.LocalDateTime;

public class NewsQuery {
    private int size;
    private DateTime startDate;
    private DateTime endDate;
    private String words;
    private String categories;

    public NewsQuery() {
        this.size = 20;
        this.startDate = null;
        this.endDate = null;
        this.words = null;
        this.categories = null;
    }

    public int getSize() {
        return this.size;
    }

    public NewsQuery setSize(int size) {
        this.size = size;
        return this;
    }

    public DateTime getStartDate() {
        return this.startDate;
    }

    public NewsQuery setStartDate(DateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public DateTime getEndDate() {
        return this.endDate;
    }

    public NewsQuery setEndDate(DateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getWords() {
        return this.words;
    }

    public NewsQuery setWords(String words) {
        this.words = words;
        return this;
    }

    public String getCategories() {
        return this.categories;
    }

    public NewsQuery setCategories(String categories) {
        this.categories = categories;
        return this;
    }
}
