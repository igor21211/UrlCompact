package com.example.urlcompact.CustomServiceTest;

import com.example.urlcompact.dto.UrlDto;
import com.example.urlcompact.dto.UrlDtoMapper;
import com.example.urlcompact.exceptions.InvalidUrlException;
import com.example.urlcompact.exceptions.UrlNotFoundException;
import com.example.urlcompact.model.Url;
import com.example.urlcompact.services.UrlCustomService;
import com.example.urlcompact.services.UrlService;
import com.example.urlcompact.validations.UrlValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlCustomServiceTest {

    private UrlCustomService underTest;

    @Mock
    private UrlService service;
    @Mock
    private UrlValid valid;

    private UrlDtoMapper mapper = new UrlDtoMapper();

    private static final String FULL_URL = "https://example.com";
    private static final String COMPACT_URL = "https://exa/com";

    @BeforeEach
    void setUp(){
        underTest = new UrlCustomService(service,mapper, valid);
    }

    @Test
    void generateCompactTest(){
        when(valid.validateUrl(FULL_URL)).thenReturn(true);
        when(service.existFullUrl(FULL_URL)).thenReturn(false);
        Url url = new Url();
        url.setFullUrl(FULL_URL);
        UrlDto result =  underTest.generateCompact(url);
        verify(valid).validateUrl(FULL_URL);
        verify(service).existFullUrl(FULL_URL);
        verify(service).generateShortUrl(any());
        assertThat(result.getCompactUrl()).isNotNull();
        assertNotNull(result);
    }

    @Test
    void generateCompactUrlWithNotValidUrl(){
        when(valid.validateUrl(FULL_URL)).thenReturn(false);
        Url url = new Url();
        url.setFullUrl(FULL_URL);
        assertThatThrownBy(()-> underTest.generateCompact(url))
                .isInstanceOf(InvalidUrlException.class)
                .hasMessage("Invalid Url");
        verify(service, never()).generateShortUrl(any());
    }

    @Test
    void generateCompactUrlWithUrlWhoHasInDB(){
        when(valid.validateUrl(FULL_URL)).thenReturn(true);
        when(service.existFullUrl(FULL_URL)).thenReturn(true);
        Url url = new Url();
        url.setFullUrl(FULL_URL);
        url.setCompactUrl(COMPACT_URL);
        when(service.getByFullUrl(FULL_URL)).thenReturn(Optional.of(url));
        UrlDto expected = mapper.apply(url);
        UrlDto actual = underTest.generateCompact(url);
        assertThat(actual.getFullUrl()).isEqualTo(expected.getFullUrl());
        assertThat(actual.getCompactUrl()).isEqualTo(expected.getCompactUrl());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void selectCompactUrlToTakeFullUrl(){
        Url url = new Url();
        url.setCompactUrl(COMPACT_URL);
        when(service.getByCompactUrl(COMPACT_URL)).thenReturn(Optional.of(url));
        UrlDto expected = mapper.apply(url);
        UrlDto actual = underTest.regenerateUrl(COMPACT_URL);
        assertThat(actual.getFullUrl()).isEqualTo(expected.getFullUrl());
        assertThat(actual.getCompactUrl()).isEqualTo(expected.getCompactUrl());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void cannotFindCompactUrl(){
        Url url = new Url();
        url.setCompactUrl(COMPACT_URL);
        when(service.getByCompactUrl(COMPACT_URL)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> underTest.regenerateUrl(COMPACT_URL))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessage("Something wrong cannot find url :" + COMPACT_URL);
        verify(service,times(1) ).getByCompactUrl(COMPACT_URL);
    }

    @Test
    void regenerateCompactUrl(){
        when(valid.validateUrl(FULL_URL)).thenReturn(true);
        Url url = new Url();
        url.setFullUrl(FULL_URL);
        UrlDto result =  underTest.regenerateCompactUrl(url.getFullUrl());
        verify(service).deleteByFullUrl(FULL_URL);
        verify(valid).validateUrl(FULL_URL);
        verify(service).generateShortUrl(any());
        assertThat(result.getCompactUrl()).isNotNull();
        assertNotNull(result);
    }
}
