package com.homework.spring_short_url.dto;




public class UrlResultDTO extends UrlDTO { // похідний клас від UrlDTO у якого ще є одне поле

    protected String shortUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
