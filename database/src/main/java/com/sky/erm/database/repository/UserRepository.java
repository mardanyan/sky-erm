package com.sky.erm.database.repository;

import com.sky.erm.database.record.UserRecord;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

/**
 * Repository for users.
 */
@Repository
public interface UserRepository extends CrudRepository<UserRecord, Long> {

    Optional<UserRecord> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserRecord> findWithLockById(Long id);

}
