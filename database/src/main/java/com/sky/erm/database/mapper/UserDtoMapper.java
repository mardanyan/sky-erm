package com.sky.erm.database.mapper;

import com.sky.erm.database.record.UserRecord;
import com.sky.erm.dto.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Maps between {@link User} DTOs and records.
 */
@Mapper( nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface UserDtoMapper extends DtoMapper<User, UserRecord> {

    @Override
    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    UserRecord toRecord(User user);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    void update(User user, @MappingTarget UserRecord userRecord);

}
