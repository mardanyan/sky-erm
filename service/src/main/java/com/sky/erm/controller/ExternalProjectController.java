package com.sky.erm.controller;

import com.sky.erm.dto.ExternalProject;
import com.sky.erm.service.ExternalProjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/" + ExternalProjectController.ENDPOINT)
@AllArgsConstructor
@Slf4j
public class ExternalProjectController {

    public static final String ENDPOINT = "external-project";

    private final ExternalProjectService externalProjectService;

    @Operation(summary = "Add a new external project")
    @PostMapping
    public ExternalProject addExternalProject(@RequestBody ExternalProject externalProject) {
        log.info("Adding external project: {}", externalProject);
        return externalProjectService.addExternalProject(externalProject);
    }

    @Operation(summary = "Get an external projects by user id")
    @GetMapping("user-id/{userId}")
    public List<ExternalProject> getExternalProjectsForUserId(@PathVariable Long userId) {
        log.info("Getting external projects for userId: {}", userId);
        return externalProjectService.getExternalProjectsForUserId(userId);
    }

    @Operation(summary = "Get an external projects by user id")
    @GetMapping("{id}")
    public ExternalProject getExternalProject(@PathVariable Long id) {
        log.info("Getting external project with id: {}", id);
        return externalProjectService.getExternalProject(id);
    }

    @Operation(summary = "Delete an external project by external project id")
    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteExternalProjects(@PathVariable Long id) {
        log.info("Deleting external project with id: {}", id);
        externalProjectService.deleteExternalProjects(id);
    }

    @Operation(summary = "Get all external projects")
    @GetMapping()
    public List<ExternalProject> getAllExternalProjects() {
        log.info("Getting all external projects");
        return externalProjectService.getAllExternalProjects();
    }

}
