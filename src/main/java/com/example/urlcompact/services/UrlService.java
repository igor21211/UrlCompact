package com.example.urlcompact.services;

import com.example.urlcompact.dto.UrlDto;
import com.example.urlcompact.model.Url;

import java.util.Optional;

public interface UrlService {
    Optional<Url> getByFullUrl(String fullUrl);
    Optional<Url>  getByCompactUrl(String compactUrl);
    Url  generateShortUrl(Url url);
    boolean existFullUrl(String fullUrl);
    void deleteByFullUrl(String fullUrl);
}
