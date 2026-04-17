package com.URLShortener.URLS.Repository;


import com.URLShortener.URLS.Entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    Optional<UrlEntity> findByShortCode(String shortCode);

    boolean existsByShortCode(String customAlias);

    Optional<UrlEntity> findFirstByLongCodeAndIsCustomAliasFalseOrderByCreatedAtAsc(String longCode);

}
