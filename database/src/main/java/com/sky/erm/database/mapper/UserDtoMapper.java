package com.sky.erm.database.mapper;

import com.sky.erm.database.record.UserRecord;
import com.sky.erm.dto.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Maps between {@link User} DTOs and records.
 */
@Mapper
public interface UserDtoMapper extends DtoMapper<User, UserRecord> {

    @Override
    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    UserRecord toRecord(User user);

}
