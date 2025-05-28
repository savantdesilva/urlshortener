package com.api.urlshortener.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class UrlValidatorUtil {

    public static boolean isValidUrl(String url) {
        String validUrlPattern = "^https?:\\/\\/"
                + "([a-z0-9]+(-[a-z0-9]+)*\\.)+"
                + "(com(\\.au)?|[a-z]{2,6})"
                + "(\\/[^\\s]*)?$";
        Pattern pattern = Pattern.compile(validUrlPattern);

        return StringUtils.hasText(url) && pattern.matcher(url).matches();
    }
}
