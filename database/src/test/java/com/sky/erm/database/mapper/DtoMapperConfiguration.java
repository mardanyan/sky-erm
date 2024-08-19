package com.sky.erm.database.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DtoMapperConfiguration {

    @Bean
    public UserDtoMapper userDtoMapper() {
        return Mappers.getMapper(UserDtoMapper.class);
    }

    @Bean
    public ExternalProjectDtoMapper externalProjectDtoMapper() {
        return Mappers.getMapper(ExternalProjectDtoMapper.class);
    }

}
