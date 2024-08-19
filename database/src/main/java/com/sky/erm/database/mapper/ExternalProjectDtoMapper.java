package com.sky.erm.database.mapper;

import com.sky.erm.database.record.ExternalProjectRecord;
import com.sky.erm.dto.ExternalProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Maps between {@link ExternalProject} DTOs and records.
 */
@Mapper
public interface ExternalProjectDtoMapper extends DtoMapper<ExternalProject, ExternalProjectRecord> {

    @Override
    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    ExternalProjectRecord toRecord(ExternalProject dto);

}
