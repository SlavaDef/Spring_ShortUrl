package com.homework.spring_short_url.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.homework.spring_short_url.dto.UrlDTO;
import com.homework.spring_short_url.dto.UrlStatDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class UrlRecord {
    @Id
    @GeneratedValue
    private Long id;

  //  @Column(nullable = false)
    private String url; // long URL довгий урл

   // @Column(nullable = false)  // поле рахує скільки раз переходять за скороченим посиланням
    private Long count;

    @Temporal(value = TemporalType.TIMESTAMP) // каже базі як зберігати дату в базі - тільки час тільки дату чи все
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
 //   @Column(nullable = false)
    private LocalDateTime lastAccess;

    private String shortUrl;

    public UrlRecord(String url) { // в конструкторах ініціалізуємо певні значення за замовчуванням
        count = 0L;
        lastAccess = LocalDateTime.now();
        this.url=url;
        shortUrl="";
    }


   /* public static UrlRecord of(UrlDTO urlDTO) { // це метод який копіює данні з ентеті в дто

        return new UrlRecord(urlDTO.getUrl());
    } */


    public UrlStatDTO toStatDTO() {
        UrlStatDTO result = new UrlStatDTO();

        result.setUrl(url); // копіюємо довгий url
        result.setShortUrl(shortUrl);
        result.setRedirects(count);
        result.setLastAccess(lastAccess);

        return result;
    }
}
