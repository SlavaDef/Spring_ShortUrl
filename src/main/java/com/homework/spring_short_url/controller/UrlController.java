package com.homework.spring_short_url.controller;

import com.homework.spring_short_url.dto.UrlDTO;
import com.homework.spring_short_url.dto.UrlResultDTO;
import com.homework.spring_short_url.dto.UrlStatDTO;
import com.homework.spring_short_url.service.UrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class UrlController {

    private final UrlService urlService; // інжектимо сервіс

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/")
    public String hello(){
        return " Hello dear user";
    }

    @GetMapping("shorten_simple2")
    public UrlResultDTO shorten2(@RequestParam String url) { // Jackson / GSON сюди приходить в метод
        UrlDTO urlDTO = new UrlDTO(); // create urlDTO
        urlDTO.setUrl(url); // передаємо який юрл треба скоротити

        String id = urlService.saveUrl2(urlDTO); // зберігаємо дтошку в базі

        UrlResultDTO result = new UrlResultDTO(); // новий обьект вже іншої дтошки
        result.setUrl(urlDTO.getUrl()); // першому полю встановили значення що передавав юзер
        result.setShortUrl(id); // другому полю передали id
// в рест контроллері при повернені обьекту він автоматично серіалізується в джейсон формат
        return result; // повертаємо id збереженого обьекту чи іншу дтошку з двома обьектами
    }

    // postmen
    // http://localhost:8080/shorten_simple?url=https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=EUR&date=20240101&json
    // в результаті отримаємо GSON - що скорочуємо і як скоротили
    @GetMapping("shorten_simple")
    public UrlResultDTO shorten(@RequestParam String url) { // Jackson / GSON сюди приходить в метод
        UrlDTO urlDTO = new UrlDTO(); // create urlDTO
        urlDTO.setUrl(url); // передаємо який юрл треба скоротити

        long id = urlService.saveUrl(urlDTO); // зберігаємо дтошку в базі

        var result = new UrlResultDTO(); // новий обьект вже іншої дтошки
        result.setUrl(urlDTO.getUrl()); // першому полю встановили значення що передавав юзер
        result.setShortUrl(Long.toString(id)); // другому полю передали id
// в рест контроллері при повернені обьекту він автоматично серіалізується в джейсон формат
        return result; // повертаємо id збереженого обьекту чи іншу дтошку з двома обьектами
    }

    // @RequestBody UrlDTO urlDTO) ніби кажуть автоматична десерилізація з джейсона в дто
    @PostMapping("shorten") // тут альтернативна реалізація скорочення, приходить вже запит в json
    public UrlResultDTO shorten(@RequestBody UrlDTO urlDTO) { // Jackson / GSON
        long id = urlService.saveUrl(urlDTO);

        UrlResultDTO result = new UrlResultDTO();
        result.setUrl(urlDTO.getUrl());
        result.setShortUrl(Long.toString(id));

        return result;
    }

    /*
        302
        Location: https://goto.com
        Cache-Control: no-cache, no-store, must-revalidate
     */

    @GetMapping("my/{id}")
    public ResponseEntity<Void> redirect(@PathVariable("id") Long id) {
        String url = urlService.getUrl(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        headers.setCacheControl("no-cache, no-store, must-revalidate");

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("stat")
    public List<UrlStatDTO> stat() {
        return urlService.getStatistics();
    }
}
