package com.sky.erm.database.mapper;

import com.sky.erm.database.record.ExternalProjectRecord;
import com.sky.erm.dto.ExternalProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import(DtoMapperConfiguration.class)
class ExternalProjectDtoMapperTest {

    @Autowired
    private ExternalProjectDtoMapper externalProjectDtoMapper;

    @Test
    void externalProject_convertToExternalProjectRecord_returnsCorrectValues() {
        ExternalProject externalProject = ExternalProject.builder()
                .id(1L)
                .userId(2L)
                .name("name")
                .build();

        ExternalProjectRecord externalProjectRecord = externalProjectDtoMapper.toRecord(externalProject);
        assertThat(externalProjectRecord.getId()).isNull();
        assertThat(externalProjectRecord.getUserId()).isEqualTo(2L);
        assertThat(externalProjectRecord.getName()).isEqualTo("name");
    }

    @Test
    void externalProjectRecord_convertToExternalProject_returnsCorrectValues() {
        ExternalProjectRecord externalProjectRecord = ExternalProjectRecord.builder()
                .id(1L)
                .userId(2L)
                .name("name")
                .build();

        ExternalProject externalProject = externalProjectDtoMapper.fromRecord(externalProjectRecord);
        assertThat(externalProject.getId()).isOne();
        assertThat(externalProject.getUserId()).isEqualTo(2L);
        assertThat(externalProject.getName()).isEqualTo("name");
    }

}
