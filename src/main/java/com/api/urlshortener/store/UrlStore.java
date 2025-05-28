package com.api.urlshortener.store;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class UrlStore {

    private final Map<String, String> shortCodeUrlMap = new ConcurrentHashMap<>();

    public boolean storeNewUrl(String shortCode, String url) throws Exception {
        if (shortCodeUrlMap.isEmpty()
                || (!shortCodeUrlMap.containsKey(shortCode) && !shortCodeUrlMap.containsValue(url))) {
            shortCodeUrlMap.put(shortCode, url);
            log.info("Added new url {} to the url store", url);
            return true;
        } else {
            log.info("Short Code or Url already exists. reusing..");
        }
        return false;
    }

    public String fetchOriginalUrl(String shortCode) {
        return shortCodeUrlMap.get(shortCode);
    }

    public String fetchShortCode(String originalUrl) {
        for (Map.Entry<String, String> entry : shortCodeUrlMap.entrySet()) {
            if (Objects.equals(entry.getValue(), originalUrl)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
