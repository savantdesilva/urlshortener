package com.api.urlshortener.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlValidatorUtilTest {

    @Test
    void validUrlsShouldPass() {
        assertTrue(UrlValidatorUtil.isValidOriginalUrl("http://origin.com.au"));
        assertTrue(UrlValidatorUtil.isValidOriginalUrl("https://origin.com.au"));
        assertTrue(UrlValidatorUtil.isValidOriginalUrl("http://origin.com.au/index.html"));
        assertTrue(UrlValidatorUtil.isValidOriginalUrl("https://origin.com.au/index.html"));
        assertTrue(UrlValidatorUtil.isValidOriginalUrl("https://domain.com"));
        assertTrue(UrlValidatorUtil.isValidOriginalUrl("https://sub.domain.com.au/path/to/page"));
    }

    @Test
    void invalidUrlsShouldFail() {
        assertFalse(UrlValidatorUtil.isValidOriginalUrl(null));
        assertFalse(UrlValidatorUtil.isValidOriginalUrl(""));
        assertFalse(UrlValidatorUtil.isValidOriginalUrl("ftp://domain.com"));
        assertFalse(UrlValidatorUtil.isValidOriginalUrl("httpshh://domain.com"));
        assertFalse(UrlValidatorUtil.isValidOriginalUrl("https://.cc.sd"));
        assertFalse(UrlValidatorUtil.isValidOriginalUrl("https://domain..com"));
        assertFalse(UrlValidatorUtil.isValidOriginalUrl("http://domain.c"));
        assertFalse(UrlValidatorUtil.isValidOriginalUrl("http://domain.123"));
        assertFalse(UrlValidatorUtil.isValidOriginalUrl("https://domain"));
    }
}
