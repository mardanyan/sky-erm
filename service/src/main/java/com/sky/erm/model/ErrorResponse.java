package com.sky.erm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Response object for error responses.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    HttpStatus httpStatus;

    String message;

}
