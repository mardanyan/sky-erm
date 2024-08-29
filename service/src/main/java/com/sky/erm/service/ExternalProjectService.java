package com.sky.erm.service;

import com.sky.erm.database.mapper.ExternalProjectDtoMapper;
import com.sky.erm.database.record.ExternalProjectRecord;
import com.sky.erm.database.repository.ExternalProjectRepository;
import com.sky.erm.dto.ExternalProject;
import com.sky.erm.exception.ExternalProjectNotFoundException;
import com.sky.erm.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Service for external project operations.
 */
@Service
@AllArgsConstructor
public class ExternalProjectService {

    private final ExternalProjectRepository externalProjectRepository;

    private final ExternalProjectDtoMapper externalProjectDtoMapper;

    private final UserService userService;

    public ExternalProject getExternalProject(Long id) {
        ExternalProjectRecord externalProjectRecord = externalProjectRepository.findById(id)
                .orElseThrow(ExternalProjectNotFoundException::new);

        return externalProjectDtoMapper.fromRecord(externalProjectRecord);
    }

    public ExternalProject addExternalProject(ExternalProject externalProject) {
        ExternalProjectRecord externalProjectRecord = externalProjectDtoMapper.toRecord(externalProject);
        ExternalProjectRecord savedExternalProjectRecord = externalProjectRepository.save(externalProjectRecord);
        return externalProjectDtoMapper.fromRecord(savedExternalProjectRecord);
    }

    public void deleteExternalProjects(Long id) {
        if (!externalProjectRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        externalProjectRepository.deleteById(id);
    }

    public List<ExternalProject> getAllExternalProjects() {
        return StreamSupport.stream(externalProjectRepository.findAll().spliterator(), false)
                .map(externalProjectDtoMapper::fromRecord)
                .toList();
    }

    public List<ExternalProject> getExternalProjectsForUserId(Long userId) {
        if (userService.getUser(userId) == null) {
            throw new UserNotFoundException();
        }
        return externalProjectRepository.findByUserId(userId).stream()
                .map(externalProjectDtoMapper::fromRecord)
                .toList();
    }

}
