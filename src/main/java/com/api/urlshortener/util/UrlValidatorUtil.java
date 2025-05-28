package com.api.urlshortener.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class UrlValidatorUtil {

    public static boolean isValidUrl(String url) {
        String validUrlPattern = "^(http|https)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/.*)?$";
        Pattern pattern = Pattern.compile(validUrlPattern);

        return StringUtils.hasText(url) && pattern.matcher(url).matches();
    }
}
