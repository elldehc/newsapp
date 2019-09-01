package com.java.liqibin.model.bean;

public class NewsQuery {
    private int size;
    private DateTime startDate;
    private DateTime endDate;
    private String words;
    private String categories;
    private int page;

    public NewsQuery() {
        this.size = 15;
        this.startDate = null;
        this.endDate = DateTime.now();
        this.words = null;
        this.categories = null;
        this.page = 1;
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

    public int getPage() {
        return page;
    }

    public NewsQuery setPage(int page) {
        this.page = page;
        return this;
    }
}
