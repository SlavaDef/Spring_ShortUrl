package com.homework.spring_short_url.repo;

import com.homework.spring_short_url.dto.UrlResultDTO;
import com.homework.spring_short_url.models.UrlRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlRecord, Long> {

    UrlRecord findByUrl(String url);
    // якщо вірно назвати то данний метод спрінг рахує як урл ( String url - парами запиту)

    UrlRecord findByShortUrl(String shortUrl);
}
