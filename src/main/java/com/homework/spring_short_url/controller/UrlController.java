package com.homework.spring_short_url.controller;

import com.homework.spring_short_url.dto.UrlResultDTO;
import com.homework.spring_short_url.dto.UrlStatDTO;
import com.homework.spring_short_url.models.UrlRecord;
import com.homework.spring_short_url.service.UrlService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
public class UrlController {

    private static final Logger LOGGER = LogManager.getLogger(UrlController.class);

    private final UrlService urlService; // інжектимо сервіс


    @GetMapping("/")
    public String hello() {
        return " Hello dear user";
    }


    @GetMapping("shorten_simple")
    public UrlResultDTO shorten(@RequestParam String url) {
        UrlRecord urlRecord = urlService.createUrl(url);

        UrlResultDTO urlResultDTO = new UrlResultDTO();
        urlResultDTO.setUrl(url);
        urlResultDTO.setShortUrl(urlRecord.getShortUrl());

        return urlResultDTO; // return ResultDto in which we copy url and shortlink
    }

    @GetMapping("my/{link}")
    public ResponseEntity<Void> redirect(@PathVariable("link") String link) {

        String url = urlService.getUrl(link).getUrl();

        LOGGER.info("url = "+ url);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        headers.setCacheControl("no-cache, no-store, must-revalidate"); // це відрубити кушування в браузері

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // @RequestBody UrlDTO urlDTO) ніби кажуть автоматична десерилізація з джейсона в дто
   /* @PostMapping("shorten") // тут альтернативна реалізація скорочення, приходить вже запит в json
    public UrlResultDTO shorten() { // Jackson / GSON @RequestBody UrlDTO urlDTO)
        UrlRecord urlRecord = urlService.saveUrl();

        UrlResultDTO result = new UrlResultDTO();
        result.setUrl(urlRecord.getUrl());
        result.setShortUrl(urlRecord.getShortUrl());

        return result;
    } */

    @GetMapping("stat")
    public List<UrlStatDTO> stat() {
        return urlService.getStatistics();
    }

    @GetMapping("getAll")
    public List<UrlRecord> all() {
        return urlService.listAll();
    }

}
