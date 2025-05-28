package com.api.urlshortener.controller;

import com.api.urlshortener.model.UrlRequest;
import com.api.urlshortener.model.UrlResponse;
import com.api.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UrlControllerTest {

    @Mock
    private UrlShortenerService urlShortenerService;
    @InjectMocks
    private UrlController urlController;


    @Test
    void buildShortUrl_shouldReturnShortUrlResponse() throws Exception {
        String inputUrl = "https://example.com";
        String shortUrl = "http://short.ly/abc123";

        UrlRequest request = new UrlRequest();
        request.setUrl(inputUrl);

        when(urlShortenerService.generateShortUrl(inputUrl)).thenReturn(shortUrl);

        ResponseEntity<UrlResponse> response = urlController.buildShortUrl(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(shortUrl, response.getBody().getResponse());
    }

    @Test
    void redirectFromShortUrl_shouldReturn302WhenFound() {
        String shortUrl = "http://short.ly/abc123";
        String originalUrl = "https://example.com";

        when(urlShortenerService.fetchOriginalUrl(shortUrl)).thenReturn(originalUrl);

        ResponseEntity<?> response = urlController.redirectFromShortUrl(shortUrl);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(URI.create(originalUrl), response.getHeaders().getLocation());
    }

    @Test
    void redirectFromShortUrl_shouldReturn404WhenNotFound() {
        String shortUrl = "http://short.ly/xyz";

        when(urlShortenerService.fetchOriginalUrl(shortUrl)).thenReturn(null);

        ResponseEntity<?> response = urlController.redirectFromShortUrl(shortUrl);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getUrlInfo_shouldReturnMapWhenExists() {
        String shortUrl = "http://short.ly/abc123";
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("abc123", "https://example.com");

        when(urlShortenerService.fetchUrlInfo(shortUrl)).thenReturn(map);

        ResponseEntity<?> response = urlController.getUrlInfo(shortUrl);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ConcurrentHashMap);
    }

    @Test
    void getUrlInfo_shouldReturn404WhenEmpty() {
        String shortUrl = "http://short.ly/abc123";
        ConcurrentHashMap<String, String> emptyMap = new ConcurrentHashMap<>();

        when(urlShortenerService.fetchUrlInfo(shortUrl)).thenReturn(emptyMap);

        ResponseEntity<?> response = urlController.getUrlInfo(shortUrl);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof UrlResponse);
    }
}
