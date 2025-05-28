package com.api.urlshortener.model;

import lombok.Data;

@Data
public class UrlResponse {
    private int reponseStatus;
    private String response;

    public UrlResponse(int httpStatus, String responseData) {
        this.reponseStatus = httpStatus;
        this.response = responseData;
    }
}
