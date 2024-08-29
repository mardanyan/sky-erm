package com.sky.erm.service;

import com.sky.erm.database.mapper.UserDtoMapper;
import com.sky.erm.database.record.UserRecord;
import com.sky.erm.database.repository.UserRepository;
import com.sky.erm.dto.User;
import com.sky.erm.dto.PasswordProtectedUser;
import com.sky.erm.exception.UserAlreadyExistsException;
import com.sky.erm.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Service for user operations.
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserDtoMapper userDtoMapper;

    private final PasswordEncoder passwordEncoder;

    public User addUser(User user) {
        UserRecord userRecord = userDtoMapper.toRecord(user);
        updatePasswordEncoded(userRecord);
        UserRecord savedUserRecord;
        try {
            savedUserRecord = userRepository.save(userRecord);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException(user.getEmail(), e);
        }
        User savedUser = userDtoMapper.fromRecord(savedUserRecord);
        return new PasswordProtectedUser(savedUser);
    }

    /**
     * All projects related to a user will be deleted automatically
     *
     * @param userId the user id
     */
    public void deleteUser(Long userId) {
        UserRecord userRecord = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        preventSelfDeletion(userRecord.getEmail());
        userRepository.deleteById(userId);
    }

    public User getUser(Long userId) {
        User user = userDtoMapper.fromRecord(findUserById(userId));
        return new PasswordProtectedUser(user);
    }

    public User getUserByEmail(String userEmail) {
        User user = userDtoMapper.fromRecord(findUserByEmail(userEmail));
        return new PasswordProtectedUser(user);
    }

    @Transactional
    public User updateUser(Long userId, User user) {
        UserRecord userRecord = findWithLockUserById(userId);

        updatePasswordEncoded(user);

        userDtoMapper.update(user, userRecord);

        UserRecord updatedUserRecord = userRepository.save(userRecord);
        return new PasswordProtectedUser(userDtoMapper.fromRecord(updatedUserRecord));
    }

    // TODO: add pagination
    public List<? extends User> getUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(userDtoMapper::fromRecord)
                .map(PasswordProtectedUser::new)
                .toList();
    }

    private UserRecord findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    private UserRecord findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    private UserRecord findWithLockUserById(Long id) {
        return userRepository.findWithLockById(id).orElseThrow(UserNotFoundException::new);
    }

    private void preventSelfDeletion(String userEmail) {
        // TODO: implement
    }

    private void updatePasswordEncoded(UserRecord userRecord) {
        String plainPassword = userRecord.getPassword();
        if (plainPassword != null) {
            userRecord.setPassword(passwordEncoder.encode(plainPassword));
        }
    }

    private void updatePasswordEncoded(User user) {
        String plainPassword = user.getPassword();
        if (plainPassword != null) {
            user.setPassword(passwordEncoder.encode(plainPassword));
        }
    }

}
