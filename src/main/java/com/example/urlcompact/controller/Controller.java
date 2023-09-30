package com.example.urlcompact.controller;

import com.example.urlcompact.dto.UrlDto;
import com.example.urlcompact.model.Url;
import com.example.urlcompact.services.UrlCustomService;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/")
public class Controller {
    private final UrlCustomService service;

    @PostMapping("generate")
    public ResponseEntity<?> generateShortUrl(@RequestBody Url url){
        UrlDto urlNew = service.generateCompact(url);
        return ResponseEntity.ok(urlNew);
    }

    @PostMapping("regenerate")
    public ResponseEntity<?> regenerateUrl(@RequestBody Url url){
        UrlDto urlNew = service.regenerateUrl(url.getCompactUrl());
        return ResponseEntity.ok(urlNew);
    }

    @PostMapping("regenerateCompact")
    public ResponseEntity<?> regenerateCompactUrl(@RequestBody Url url){
        UrlDto urlNew = service.regenerateCompactUrl(url.getFullUrl());
        return ResponseEntity.ok(urlNew);
    }
}
