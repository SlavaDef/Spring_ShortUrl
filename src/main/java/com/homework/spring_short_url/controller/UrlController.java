package com.homework.spring_short_url.controller;

import com.homework.spring_short_url.dto.UrlDTO;
import com.homework.spring_short_url.dto.UrlResultDTO;
import com.homework.spring_short_url.service.UrlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
