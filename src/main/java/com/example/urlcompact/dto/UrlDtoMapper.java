package com.example.urlcompact.dto;

import com.example.urlcompact.model.Url;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UrlDtoMapper implements Function<Url, UrlDto> {
    @Override
    public UrlDto apply(Url url) {
        return new UrlDto(
                url.getFullUrl(),
                url.getCompactUrl()
        );
    }
}
