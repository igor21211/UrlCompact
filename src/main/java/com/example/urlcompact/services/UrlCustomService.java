package com.example.urlcompact.services;

import com.example.urlcompact.dto.UrlDto;
import com.example.urlcompact.dto.UrlDtoMapper;
import com.example.urlcompact.exceptions.InvalidUrlException;
import com.example.urlcompact.exceptions.UrlNotFoundException;
import com.example.urlcompact.model.Url;
import com.example.urlcompact.validations.UrlValid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;


@Service
@Slf4j
@AllArgsConstructor
public class UrlCustomService {
    private final UrlService service;
    private final UrlDtoMapper urlDtoMapper;
    private final UrlValid urlValid;

    public UrlDto generateCompact(Url url){
        if(!urlValid.validateUrl(url.getFullUrl())){
            log.info("Url {} not valid",url);
            throw new InvalidUrlException("Invalid Url");
        }
        Url urlNew = new Url();
        if(!service.existFullUrl(url.getFullUrl())){
            urlNew.setFullUrl(url.getFullUrl());
            urlNew.setCompactUrl(generateCompactUrl());
            service.generateShortUrl(urlNew);
            log.info("Url {} save to DB",url);
        }else {
         urlNew = getByFullUrl(url.getFullUrl());
        }
        return urlDtoMapper.apply(urlNew);
    }

    public Url getByFullUrl(String fullUrl){
        log.info("GetByfullUrl {}", fullUrl);
        return  service.getByFullUrl(fullUrl)
                .orElseThrow(() -> new UrlNotFoundException("Something wrong cannot find url :" + fullUrl));
    }

    public UrlDto regenerateUrl(String compactUrl){
        return  urlDtoMapper.apply(service.getByCompactUrl(compactUrl)
                .orElseThrow(() -> new UrlNotFoundException("Something wrong cannot find url :" + compactUrl)));
    }

    private  String generateCompactUrl(){
        String baseUrl = "http://url.co/";
        String randomUrlPartTwo = RandomStringUtils.randomAlphabetic(3).toLowerCase();
        return baseUrl+randomUrlPartTwo;
    }

    public UrlDto regenerateCompactUrl(String fullUrl){
        log.info("delete url {}", fullUrl);
        service.deleteByFullUrl(fullUrl);
        Url url = new Url();
        url.setFullUrl(fullUrl);
        if(!urlValid.validateUrl(url.getFullUrl())){
            log.info("Url {} not valid",url);
            throw new InvalidUrlException("Invalid Url");
        }
        url.setCompactUrl(generateCompactUrl());
        service.generateShortUrl(url);
        log.info("Url {} save to DB",url);
        return urlDtoMapper.apply(url);
    }

}
