package com.sky.erm.database.mapper;

import com.sky.erm.database.record.UserRecord;
import com.sky.erm.dto.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import(DtoMapperConfiguration.class)
class UserDtoMapperTest {

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Test
    void user_convertToUserRecord_returnsCorrectValues() {
        User user = User.builder()
                .id(1L)
                .email("email")
                .password("password")
                .name("name")
                .build();

        UserRecord userRecord = userDtoMapper.toRecord(user);
        assertThat(userRecord.getId()).isNull();
        assertThat(userRecord.getEmail()).isEqualTo("email");
        assertThat(userRecord.getPassword()).isEqualTo("password");
        assertThat(userRecord.getName()).isEqualTo("name");
    }

    @Test
    void userRecord_convertToUser_returnsCorrectValues() {
        UserRecord userRecord = UserRecord.builder()
                .id(1L)
                .email("email")
                .password("password")
                .name("name")
                .build();

        User user = userDtoMapper.fromRecord(userRecord);
        assertThat(user.getId()).isOne();
        assertThat(user.getEmail()).isEqualTo("email");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("name");
    }

}
