package com.example.urlcompact.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String s) {
        super(s);
    }
}
