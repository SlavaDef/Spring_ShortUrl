package com.homework.spring_short_url.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class UrlStatDTO extends UrlResultDTO{

    private long redirects;
    private LocalDateTime lastAccess; // TODO: set normal format


}
