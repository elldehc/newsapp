package com.java.liqibin.model.bean;

public class News {
    public String image;
    public String publishTime;
    public Keyword[] keywords;
    public String language;
    public String video;
    public String title;
    public When[] when;
    public String content;
    public Person[] persons;
    public String newsID;
    public String crawlTime;
    public Organization[] organizations;
    public String publisher;
    public Location[] locations;
    public Where[] where;
    public String category;
    public Who[] who;

    public class Keyword {
        public double score;
        public String word;
    }

    public class When {
        public double score;
        public String word;
    }

    public class Person {
        public int count;
        public String linkedURL;
        public String memtion;
    }

    public class Organization {
        public int count;
        public String linkedURL;
        public String mention;
    }

    public class Location {
        public double lng;
        public int count;
        public String linkedURL;
        public double lat;
        public String mention;
    }

    public class Where {
        public double score;
        public String word;
    }

    public class Who {
        public double score;
        public String word;
    }
}