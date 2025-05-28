package com.api.urlshortener.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlValidatorUtilTest {

    @Test
    void validUrlsShouldPass() {
        assertTrue(UrlValidatorUtil.isValidUrl("http://origin.com.au"));
        assertTrue(UrlValidatorUtil.isValidUrl("https://origin.com.au"));
        assertTrue(UrlValidatorUtil.isValidUrl("http://origin.com.au/index.html"));
        assertTrue(UrlValidatorUtil.isValidUrl("https://origin.com.au/index.html"));
        assertTrue(UrlValidatorUtil.isValidUrl("https://domain.com"));
        assertTrue(UrlValidatorUtil.isValidUrl("https://sub.domain.com.au/path/to/page"));
    }

    @Test
    void invalidUrlsShouldFail() {
        assertFalse(UrlValidatorUtil.isValidUrl(null));
        assertFalse(UrlValidatorUtil.isValidUrl(""));
        assertFalse(UrlValidatorUtil.isValidUrl("ftp://domain.com"));
        assertFalse(UrlValidatorUtil.isValidUrl("httpshh://domain.com"));
        assertFalse(UrlValidatorUtil.isValidUrl("https://.cc.sd"));
        assertFalse(UrlValidatorUtil.isValidUrl("https://domain..com"));
        assertFalse(UrlValidatorUtil.isValidUrl("http://domain.c"));
        assertFalse(UrlValidatorUtil.isValidUrl("http://domain.123"));
        assertFalse(UrlValidatorUtil.isValidUrl("https://domain"));
    }
}
