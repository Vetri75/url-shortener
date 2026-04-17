package com.URLShortener.URLS.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;



@Entity
@Table(name = "url", indexes = {
        @Index(name = "idx_short_code", columnList = "short_code"),
        @Index(name = "idx_long_code_custom", columnList = "long_code, is_custom_alias")
})
@Data
public class UrlEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "short_code", unique = true)
    private String shortCode;

    @Column(name = "long_code", nullable = false)
    private String longCode;

    @Column(name = "is_custom_alias", nullable = false)
    private boolean isCustomAlias = false;

    @Column(name = "click_count")
    private long clickCount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
}
