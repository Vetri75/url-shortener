package com.URLShortener.URLS.Controller;


import com.URLShortener.URLS.Service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/generate")
    public ResponseEntity<String> generateShortUrl(@RequestParam String longUrl, @RequestParam(required = false) String customAlias, @RequestParam(required = false) Integer expiryInDays, HttpServletRequest request){
        return urlService.generateShortUrl(longUrl, customAlias, expiryInDays, request);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode){
        return urlService.redirect(shortCode);
    }

    @GetMapping("/analytics/{shortCode}")
    public ResponseEntity<?> getAnalytics(@PathVariable String shortCode){
        return urlService.getAnalytics(shortCode);
    }

}
