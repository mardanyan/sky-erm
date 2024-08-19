package com.sky.erm.database.repository;

import com.sky.erm.database.record.UserRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for users.
 */
@Repository
public interface UserRepository extends CrudRepository<UserRecord, Long> {

    Optional<UserRecord> findByEmail(String email);

}
