package com.sky.erm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Represents an external project.
 */
@AllArgsConstructor
@Builder
@Data
public class ExternalProject {

    private Long id;

    private Long userId;

    private String name;

}
