package com.api.urlshortener.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UrlStoreTest {

    private UrlStore urlStore;

    @BeforeEach
    void setUp() {
        urlStore = new UrlStore();
    }

    @Test
    void storeNewUrl_Success() throws Exception {
//        Should store a new valid URL successfully
        boolean result = urlStore.storeNewUrl("abc123", "https://example.com");
        assertTrue(result);

        assertEquals("https://example.com", urlStore.fetchOriginalUrl("abc123"));
        assertEquals("abc123", urlStore.fetchShortCode("https://example.com"));
    }

    @Test
    void storeNewUrl_DuplicateFails() throws Exception {
//        Should not store duplicate shortCode or URL
        urlStore.storeNewUrl("abc123", "https://example.com");

        assertFalse(urlStore.storeNewUrl("abc123", "https://another.com"));
        assertFalse(urlStore.storeNewUrl("xyz789", "https://example.com"));
    }

    @Test
    void fetchMethodsWithMissingData() {
//        Should return null if shortCode or URL not found
        assertNull(urlStore.fetchOriginalUrl("notExist"));
        assertNull(urlStore.fetchShortCode("https://example.com"));
    }
}
