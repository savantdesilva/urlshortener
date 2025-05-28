package com.api.urlshortener.service;

import com.api.urlshortener.error.BadRequestException;
import com.api.urlshortener.error.URLNotFoundException;
import com.api.urlshortener.store.UrlStore;
import com.api.urlshortener.util.AppConstant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class UrlShortenerServiceTest {

    @Mock
    private UrlStore urlStore;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @Test
    void generateShortUrl_Success() throws Exception {
        //Test to generate new short URL for valid URL
        String originalUrl = "https://domain.com/page";
        Mockito.when(urlStore.storeNewUrl(anyString(), eq(originalUrl))).thenReturn(true);

        String shortUrl = urlShortenerService.generateShortUrl(originalUrl);
        assertNotNull(shortUrl);
        assertTrue(shortUrl.startsWith(AppConstant.SHORT_DOMAIN));
    }

    @Test
    void generateShortUrl_AlreadyExists() throws Exception {
        //Test to return existing short URL if already stored
        String originalUrl = "https://domain.com/page";
        String expectedShortCode = "abc123";

        Mockito.when(urlStore.storeNewUrl(anyString(), eq(originalUrl))).thenReturn(false);
        Mockito.when(urlStore.fetchShortCode(originalUrl)).thenReturn(expectedShortCode);

        String shortUrl = urlShortenerService.generateShortUrl(originalUrl);
        assertEquals(AppConstant.SHORT_DOMAIN + expectedShortCode, shortUrl);
    }

    @Test
    void generateShortUrl_InvalidUrl() {
        //Should throw BadRequestException for invalid URL"
        String invalidUrl = "httph://domain.com";

        Exception exception = assertThrows(BadRequestException.class, () -> {
            urlShortenerService.generateShortUrl(invalidUrl);
        });

        assertTrue(exception.getMessage().contains("invalid"));
    }

    @Test
    void fetchOriginalUrl_Success() {
//        Should fetch original URL from short URL"
        String shortCode = "abc123";
        String shortUrl = AppConstant.SHORT_DOMAIN + shortCode;
        String originalUrl = "https://domain.com";

        Mockito.when(urlStore.fetchOriginalUrl(shortCode)).thenReturn(originalUrl);

        String result = urlShortenerService.fetchOriginalUrl(shortUrl);
        assertEquals(originalUrl, result);
    }

    @Test
    void fetchOriginalUrl_NotFound() {
//        Should throw URLNotFoundException if original URL not found
        String shortCode = "abc123";
        String shortUrl = AppConstant.SHORT_DOMAIN + shortCode;

        Mockito.when(urlStore.fetchOriginalUrl(shortCode)).thenReturn(null);

        Exception exception = assertThrows(URLNotFoundException.class, () -> {
            urlShortenerService.fetchOriginalUrl(shortUrl);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void fetchUrlInfo_Success() {
//        Should fetch URL info map for existing short URL
        String shortCode = "abc123";
        String shortUrl = AppConstant.SHORT_DOMAIN + shortCode;
        String originalUrl = "https://domain.com";

        Mockito.when(urlStore.fetchOriginalUrl(shortCode)).thenReturn(originalUrl);

        ConcurrentHashMap<String, String> info = urlShortenerService.fetchUrlInfo(shortUrl);

        assertNotNull(info);
        assertEquals(1, info.size());
        assertEquals(originalUrl, info.get(shortCode));
    }

    @Test
    void fetchUrlInfo_Failure() {
//        Should return URLNotFoundException if the short URL does not exist
        String shortCode = "abc123nonexistent";
        String shortUrl = AppConstant.SHORT_DOMAIN + shortCode;

        Mockito.when(urlStore.fetchOriginalUrl(shortCode)).thenReturn(null);
        assertThrows(URLNotFoundException.class, () -> {
            urlShortenerService.fetchUrlInfo(shortUrl);
        });
    }
}