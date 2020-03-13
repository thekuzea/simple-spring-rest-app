package com.thekuzea.experimental.domain.mapper;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import com.thekuzea.experimental.api.dto.UserDto;
import com.thekuzea.experimental.domain.model.User;

import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDto;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDtoForMapper;
import static com.thekuzea.experimental.test.util.model.UserModelTestDataGenerator.createUser;

class UserMapperTest {

    @Test
    void shouldMapDtoToModel() {
        final UserDto userDto = createUserDtoForMapper();
        final User expectedUserModel = createUser();

        final User actualUserModel = UserMapper.mapToModel(userDto);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualUserModel.getUsername()).isEqualTo(expectedUserModel.getUsername());
            softly.assertThat(actualUserModel.getPassword()).isEqualTo(expectedUserModel.getPassword());
        });
    }

    @Test
    void shouldMapModelToDto() {
        final UserDto expectedUserDto = createUserDto();
        final User userModel = createUser();

        final UserDto actualUserDto = UserMapper.mapToDto(userModel);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualUserDto.getId()).isEqualTo(expectedUserDto.getId());
            softly.assertThat(actualUserDto.getUsername()).isEqualTo(expectedUserDto.getUsername());
            softly.assertThat(actualUserDto.getPassword()).isNull();
            softly.assertThat(actualUserDto.getRole()).isEqualTo(expectedUserDto.getRole());
        });
    }
}
