package com.example.urlcompact.repository;

import com.example.urlcompact.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends MongoRepository<Url, Long> {

    Optional<Url> findByFullUrl(String fullUrl);
    Optional<Url> findByCompactUrl(String compactUrl);
    boolean existsByFullUrl(String fullUrl);
    void deleteByFullUrl(String fullUrl);

}
