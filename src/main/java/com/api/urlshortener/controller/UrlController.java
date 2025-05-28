package com.api.urlshortener.controller;

import com.api.urlshortener.model.UrlRequest;
import com.api.urlshortener.model.UrlResponse;
import com.api.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @Operation(summary = "Shorten a given URL")
    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> buildShortUrl(@RequestBody UrlRequest request) throws Exception {
        String shortUrl = urlShortenerService.generateShortUrl(request.getUrl());
        UrlResponse response = new UrlResponse(HttpStatus.OK.value(), shortUrl);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Redirects to the Original URL once a Short URL is passed")
    @GetMapping("/redirect")
    public ResponseEntity<?> redirectFromShortUrl(@RequestParam("shortUrl") String shortUrl) {
        String originalUrl = urlShortenerService.fetchOriginalUrl(shortUrl);
        if (originalUrl != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(originalUrl)).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Gets the info (unique short code & original URL) of a short URL")
    @GetMapping("/info")
    public ResponseEntity<?> getUrlInfo(@RequestParam("shortUrl") String shortUrl) {
        ConcurrentHashMap<String, String> infoMap = urlShortenerService.fetchUrlInfo(shortUrl);
        if (!infoMap.isEmpty()) {
            return ResponseEntity.ok(infoMap);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UrlResponse(HttpStatus.NOT_FOUND.value(), "No URL info found."));
        }
    }
}
