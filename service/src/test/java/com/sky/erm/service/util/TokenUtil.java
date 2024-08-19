package com.sky.erm.service.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for token operations.
 */
public final class TokenUtil {

    public static HttpHeaders getHeadersForJwt(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }

}
