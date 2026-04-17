package com.URLShortener.URLS.Service;


import com.URLShortener.URLS.Entity.UrlEntity;
import com.URLShortener.URLS.Repository.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private Base62Service base62Service;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, Long> clickCountRedisTemplate;


//  Generate Short URL
    public ResponseEntity<String> generateShortUrl(String longUrl, String customAlias, Integer expiryInDays, HttpServletRequest request) {

//      Rate Limiting
        String key = "rate_limit:generate:" + getClientIp(request);
        if(!isAllowed(key, 5, 60)){
            return ResponseEntity.status(429).body("Too many requests");
        }


        // Edge case: Invalid URL
        if(longUrl == null || longUrl.isBlank() || !isValidUrl(longUrl)){
            return ResponseEntity.badRequest().body("Invalid URL");
        }

        Instant now = Instant.now();

        // Edge case: Invalid expiry date
        if(expiryInDays != null && (expiryInDays <= 0 || expiryInDays > 365)){
            return ResponseEntity.badRequest().body("Invalid expiry date");
        }

        //Expiry date
        int days = expiryInDays == null ? 7 : expiryInDays;
        Instant expiry = now.plus(days, ChronoUnit.DAYS);

        // CASE 1: Custom alias
        if (customAlias != null && !customAlias.isEmpty()) {

            if (urlRepository.existsByShortCode(customAlias)) {
                return ResponseEntity.badRequest().body("Shortcode already exists");
            }

            UrlEntity url = new UrlEntity();
            url.setLongCode(longUrl);
            url.setShortCode(customAlias);
            url.setCustomAlias(true);
            url.setCreatedAt(now);
            url.setExpiresAt(expiry);
            urlRepository.save(url);

            return ResponseEntity.ok(customAlias);
        }

        // CASE 2: Default reuse
        UrlEntity existingAlias = urlRepository
                .findFirstByLongCodeAndIsCustomAliasFalseOrderByCreatedAtAsc(longUrl)
                .orElse(null);

        if (existingAlias != null && now.isBefore(existingAlias.getExpiresAt())) {
            return ResponseEntity.ok(existingAlias.getShortCode());
        }

        // CASE 3: Create new default
        UrlEntity url = new UrlEntity();
        url.setLongCode(longUrl);
        url.setCustomAlias(false);
        url.setCreatedAt(now);
        url.setExpiresAt(expiry);
        UrlEntity saved = urlRepository.save(url);

        String shortCode = base62Service.encode(saved.getId());
        saved.setShortCode(shortCode);

        urlRepository.save(saved);

        return ResponseEntity.ok(shortCode);
    }


//  Redirect
    public ResponseEntity<?> redirect(String shortCode){

        Instant now = Instant.now();

        //Check Redis
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);

        if (cachedUrl != null) {
            System.out.println("cache hit");
            clickCountRedisTemplate.opsForValue().increment("click_count:" + shortCode);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", cachedUrl)
                    .build();
        }

        //DB fallback
        UrlEntity url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Url not found"));

        //Expiry check
        if (url.getExpiresAt() != null && now.isAfter(url.getExpiresAt())) {
            return ResponseEntity.status(HttpStatus.GONE).body("Link expired");
        }

        //Cache with TTL
        Duration ttl = Duration.between(now, url.getExpiresAt());

        if (!ttl.isNegative() && !ttl.isZero()) {
            redisTemplate.opsForValue().set(shortCode, url.getLongCode(), ttl);
        }

        //Increment analytics
        clickCountRedisTemplate.opsForValue().increment("click_count:" + shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", url.getLongCode())
                .build();

    }


//  Analytics
    public ResponseEntity<?> getAnalytics(String shortCode){
        UrlEntity url = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException(("Url not found")));
        return ResponseEntity.ok(url.getClickCount());
    }

//  Rate Limiting
    public boolean isAllowed(String key, int limit, int windowSeconds){

        Long count = clickCountRedisTemplate.opsForValue().increment(key);

        if (count == 1) {
            clickCountRedisTemplate.expire(key, Duration.ofSeconds(windowSeconds));
        }

        return count <= limit;
    }

//   Get client IP
    public String getClientIp(HttpServletRequest request){
        return request.getRemoteAddr();
    }

    public boolean isValidUrl(String url){
        try{
            URI uri = new URI(url);
            if(uri.getScheme() == null) return false;

            if(!uri.getScheme().equalsIgnoreCase("http") &&
                !uri.getScheme().equalsIgnoreCase("https")) return false;

            String host = uri.getHost();

            if(host == null || !host.contains(".") || !host.matches(".*\\.[a-zA-Z]{2,}$")) return false;

            return true;
        }catch (Exception e){
            return false;
        }
    }


//  Sync click counts
    @Scheduled(fixedRate = 10000) // Every 10 seconds
    public void syncClickCounts(){
        // Check specific pattern
        Set<String> keys = clickCountRedisTemplate.keys("click_count:*");

        for(String key : keys){
            String shortCode = key.split(":")[1];
            Long count = clickCountRedisTemplate.opsForValue().get(key);

            UrlEntity url = urlRepository.findByShortCode(shortCode).orElse(null);
            if(url != null && count != null){
                url.setClickCount(url.getClickCount() +count);
                urlRepository.save(url);
            }
            clickCountRedisTemplate.delete(key);

        }
    }

}
