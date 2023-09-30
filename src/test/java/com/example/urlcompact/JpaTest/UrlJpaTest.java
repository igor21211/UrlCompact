package com.example.urlcompact.JpaTest;

import com.example.urlcompact.model.Url;
import com.example.urlcompact.repository.UrlRepository;
import com.example.urlcompact.services.UrlJpa;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;

public class UrlJpaTest {
    private UrlJpa underTest;

    private AutoCloseable autoCloseable;
    @Mock
    private UrlRepository repository;

    private static final String FULL_URL = "https://example.com";

    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UrlJpa(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getByFullUrl(){
        underTest.getByFullUrl(FULL_URL);
        verify(repository).findByFullUrl(FULL_URL);
    }

    @Test
    void getByCompactUrl(){
        String compactUrl = "https://ex/com";
        underTest.getByCompactUrl(compactUrl);
        verify(repository).findByCompactUrl(compactUrl);
    }
    @Test
    void existFullUrl(){
        underTest.existFullUrl(FULL_URL);
        verify(repository).existsByFullUrl(FULL_URL);
    }

    @Test
    void generateShortUrl(){
        Url testurl = new Url();
        testurl.setFullUrl(FULL_URL);
        underTest.generateShortUrl(testurl);
        verify(repository).insert(testurl);
    }

    @Test
    void regenerateCompacUrl(){
        Url testurl = new Url();
        testurl.setFullUrl(FULL_URL);
        underTest.deleteByFullUrl(FULL_URL);
        verify(repository).deleteByFullUrl(FULL_URL);
    }
}
