package com.homework.spring_short_url.models;

import com.homework.spring_short_url.dto.UrlDTO;
import com.homework.spring_short_url.dto.UrlStatDTO;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class UrlRecord {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String url; // long URL довгий урл

    @Column(nullable = false)  // поле рахує скільки раз переходять за скороченим посиланням
    private Long count;

    @Temporal(value = TemporalType.TIMESTAMP) // каже базі як зберігати дату в базі - тільки час тільки дату чи все
    @Column(nullable = false)
    private Date lastAccess;

    public UrlRecord() { // в конструкторах ініціалізуємо певні значення за замовчуванням
        count = 0L;
        lastAccess = new Date();
    }

    public UrlRecord(String url) {
        this();
        this.url = url;
    }

    public static UrlRecord of(UrlDTO urlDTO) { // це метод який копіює данні з ентеті в дто

        return new UrlRecord(urlDTO.getUrl());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public UrlStatDTO toStatDTO() {
        var result = new UrlStatDTO();

        result.setUrl(url);
        result.setShortUrl(Long.toString(id));
        result.setRedirects(count);
        result.setLastAccess(lastAccess);

        return result;
    }
}
