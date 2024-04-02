package com.homework.spring_short_url.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UrlResultDTO extends UrlDTO { // похідний клас від UrlDTO у якого ще є одне поле

    protected String shortUrl;

}
