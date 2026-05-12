package com.URLShortener.URLS.Controller;


import com.URLShortener.URLS.Service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UIController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @PostMapping("/shorten-ui")
    public String shortenUrl(
            @RequestParam String longUrl,
            @RequestParam(required = false) String customAlias,
            @RequestParam(required = false) Integer expiryInDays,
            HttpServletRequest request,
            Model model
    ){

        ResponseEntity<String> response = urlService.generateShortUrl(
                longUrl,
                customAlias,
                expiryInDays,
                request
        );

        if (!response.getStatusCode().is2xxSuccessful()) {

            model.addAttribute(
                    "error",
                    response.getBody()
            );

            return "index";
        }

        String shortCode = response.getBody();

        String shortUrl =
                request.getScheme()
                        + "://"
                        + request.getServerName()
                        + "/"
                        + shortCode;

        model.addAttribute("shortUrl", shortUrl);

        return "index";
    }

    @GetMapping("/analytics-ui")
    public String analytics(
            @RequestParam String shortCode,
            Model model
    ) {

        try {

            Long clickCount = Long.parseLong(
                    urlService
                            .getAnalytics(shortCode)
                            .getBody()
                            .toString()
            );

            model.addAttribute(
                    "clickCount",
                    clickCount
            );

        } catch (Exception e) {

            model.addAttribute(
                    "analyticsError",
                    "Shortcode not found"
            );
        }

        return "index";
    }

}
