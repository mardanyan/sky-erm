package com.sky.erm.database.repository;

import com.sky.erm.database.record.ExternalProjectRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for external projects.
 */
@Repository
public interface ExternalProjectRepository extends CrudRepository<ExternalProjectRecord, Long> {

    List<ExternalProjectRecord> findByUserId(Long userId);

}
