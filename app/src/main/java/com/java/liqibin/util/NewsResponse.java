package com.java.liqibin.util;

class NewsResponse {
    int pageSize;
    int total;
    News[] data;
    int currentPage;

    class News {
        String image;
        String publishTime;
        Keyword[] keywords;
        String language;
        String video;
        String title;
        When[] when;
        String content;
        Person[] persons;
        String newsID;
        String crawlTime;
        Organization[] organizations;
        String publisher;
        Location[] locations;
        Where[] where;
        String category;
        Who[] who;

        class Keyword {
            double score;
            String word;
        }

        class When {
            double score;
            String word;
        }

        class Person {
            int count;
            String linkedURL;
            String memtion;
        }

        class Organization {
            int count;
            String linkedURL;
            String mention;
        }

        class Location {
            double lng;
            int count;
            String linkedURL;
            double lat;
            String mention;
        }

        class Where {
            int score;
            String word;
        }

        class Who {
            int score;
            String word;
        }
    }
}
