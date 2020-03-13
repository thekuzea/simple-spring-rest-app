package com.thekuzea.experimental.domain.mapper;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import com.thekuzea.experimental.api.dto.RoleDto;
import com.thekuzea.experimental.domain.model.Role;

import static org.assertj.core.api.Assertions.assertThat;

import static com.thekuzea.experimental.test.util.dto.RoleDtoTestDataGenerator.createRoleDto;
import static com.thekuzea.experimental.test.util.model.RoleModelTestDataGenerator.createUserRole;


class RoleMapperTest {

    @Test
    void shouldMapDtoToModel() {
        final RoleDto roleDto = createRoleDto();
        final Role expectedRoleModel = createUserRole();

        final Role actualRoleModel = RoleMapper.mapToModel(roleDto);

        assertThat(actualRoleModel.getName()).isEqualTo(expectedRoleModel.getName());
    }

    @Test
    void shouldMapModelToDto() {
        final RoleDto expectedRoleDto = createRoleDto();
        final Role roleModel = createUserRole();

        final RoleDto actualRoleDto = RoleMapper.mapToDto(roleModel);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualRoleDto.getId()).isEqualTo(expectedRoleDto.getId());
            softly.assertThat(actualRoleDto.getName()).isEqualTo(expectedRoleDto.getName());
        });
    }
}
