package com.sky.erm.config;

import com.sky.erm.database.mapper.ExternalProjectDtoMapper;
import com.sky.erm.database.mapper.UserDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.mapstruct.factory.Mappers;

@Configuration
public class MapperConfig {

    @Bean
    public UserDtoMapper userDtoMapper() {
        return Mappers.getMapper(UserDtoMapper.class);
    }

    @Bean
    public ExternalProjectDtoMapper externalProjectDtoMapper() {
        return Mappers.getMapper(ExternalProjectDtoMapper.class);
    }

}
