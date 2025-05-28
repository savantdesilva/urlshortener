package com.api.urlshortener.service;

import com.api.urlshortener.error.BadRequestException;
import com.api.urlshortener.error.URLNotFoundException;
import com.api.urlshortener.store.UrlStore;
import com.api.urlshortener.util.AppConstant;
import com.api.urlshortener.util.UrlValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UrlShortenerService {

    @Autowired
    private UrlStore urlStore;

    public String generateShortUrl(String originalUrl) throws Exception {
        if (UrlValidatorUtil.isValidUrl(originalUrl)) {
            log.error("Provided url {} is invalid", originalUrl);
            throw new BadRequestException(String.format("Provided url %s is invalid", originalUrl));
        }
        String uniqueCode = generateUniqueShortcode();
        boolean isUrlStored = urlStore.storeNewUrl(uniqueCode, originalUrl);

        if (isUrlStored) {
            return AppConstant.SHORT_DOMAIN + uniqueCode;
        } else {
            String existingShortCode = urlStore.fetchShortCode(originalUrl);
            return AppConstant.SHORT_DOMAIN + existingShortCode;
        }
    }

    public String fetchOriginalUrl(String shortUrl) {

        String shortCode = extractShortCodeFromUrl(shortUrl);
        String originalUrl = urlStore.fetchOriginalUrl(shortCode);
        if (!StringUtils.hasText(originalUrl)) {
            log.error("Original Url not found for shortUrl {}", shortUrl);
            throw new URLNotFoundException(String.format("Original Url not found for shortUrl %s", shortUrl));
        }

        return originalUrl;
    }

    public ConcurrentHashMap<String, String> fetchUrlInfo(String shortUrl) {
        String originalUrl = fetchOriginalUrl(shortUrl);
        if (originalUrl != null) {
            String shortCode = extractShortCodeFromUrl(shortUrl);
            return new ConcurrentHashMap<>(Map.of(shortCode, originalUrl));
        }

        return null;
    }

    protected String generateUniqueShortcode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            sb.append(AppConstant.ALPHANUM.charAt(random.nextInt(AppConstant.ALPHANUM.length())));
        }

        return sb.toString();
    }

    protected String extractShortCodeFromUrl(String shortUrl) {
        try {
            URI uri = new URI(shortUrl);
            String[] urlTokens = uri.getPath().split("/");
            return urlTokens[urlTokens.length - 1];
        } catch (URISyntaxException e) {
            throw new BadRequestException("Invalid Short URL format");
        }
    }
}
