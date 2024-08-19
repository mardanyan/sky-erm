package com.sky.erm.database.mapper;

/**
 * Maps between DTOs and records.
 *
 * @param <DTO> the DTO type
 * @param <RECORD> the record type
 */
public interface DtoMapper<DTO, RECORD> {

    RECORD toRecord(DTO dto);

    DTO fromRecord(RECORD record);

}
