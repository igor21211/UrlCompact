package com.example.urlcompact.services;

import com.example.urlcompact.dto.UrlDto;
import com.example.urlcompact.dto.UrlDtoMapper;
import com.example.urlcompact.exceptions.UrlNotFoundException;
import com.example.urlcompact.model.Url;
import com.example.urlcompact.repository.UrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static java.lang.Thread.sleep;

@Repository
@AllArgsConstructor
public class UrlJpa implements UrlService {

    private final UrlRepository repository;
    @Override
    @Cacheable(value = "url", key = "#fullUrl")
    public Optional<Url>  getByFullUrl(String fullUrl) {
        return  repository.findByFullUrl(fullUrl);
    }

    @Override
    @Cacheable(value = "compactUrl", key = "#compactUrl",unless="#result == null")
    public Optional<Url> getByCompactUrl(String compactUrl) {
        return repository.findByCompactUrl(compactUrl);
    }

    @Override
    @CacheEvict(value = "url", allEntries = true)
    public Url generateShortUrl(Url url) {
        return repository.insert(url);
    }

    @Override
    public boolean existFullUrl(String fullUrl) {
        return repository.existsByFullUrl(fullUrl);
    }

    @Override
    public void deleteByFullUrl(String fullUrl) {
        repository.deleteByFullUrl(fullUrl);
    }


}
