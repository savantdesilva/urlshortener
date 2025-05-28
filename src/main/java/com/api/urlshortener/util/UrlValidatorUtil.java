package com.api.urlshortener.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class UrlValidatorUtil {

    public static boolean isValidOriginalUrl(String url) {
        String validUrlPattern = "^https?:\\/\\/"
                + "([a-z0-9]+(-[a-z0-9]+)*\\.)+"
                + "(com(\\.au)?|[a-z]{2,6})"
                + "(\\/[^\\s]*)?$";
        Pattern pattern = Pattern.compile(validUrlPattern);

        return StringUtils.hasText(url) && pattern.matcher(url).matches();
    }

    public static boolean isValidShortUrl(String shortUrl) {
        String validUrlPattern = "^http:\\/\\/short\\.ly\\/[A-Za-z0-9]{7}$";
        Pattern pattern = Pattern.compile(validUrlPattern);

        return StringUtils.hasText(shortUrl) && pattern.matcher(shortUrl).matches();
    }
}
