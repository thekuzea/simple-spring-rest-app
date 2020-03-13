package com.thekuzea.experimental.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thekuzea.experimental.infrastructure.persistence.RoleRepository;
import com.thekuzea.experimental.api.dto.RoleDto;
import com.thekuzea.experimental.domain.model.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.ROLE_ALREADY_EXISTS;
import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.test.util.dto.RoleDtoTestDataGenerator.createRoleDto;
import static com.thekuzea.experimental.test.util.model.RoleModelTestDataGenerator.createRoleList;
import static com.thekuzea.experimental.test.util.model.RoleModelTestDataGenerator.createUserRole;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void shouldGetAllRoles() {
        final List<Role> roleList = createRoleList();
        when(roleRepository.findAll()).thenReturn(roleList);

        final List<RoleDto> actualDtoList = roleService.getAllRoles();

        verify(roleRepository).findAll();
        assertThat(actualDtoList.size()).isEqualTo(2);
    }

    @Test
    void shouldNotGetAllRoles() {
        final List<Role> roleList = Collections.emptyList();
        when(roleRepository.findAll()).thenReturn(roleList);

        final List<RoleDto> actualDtoList = roleService.getAllRoles();

        verify(roleRepository).findAll();
        assertThat(actualDtoList).isEmpty();
    }

    @Test
    void shouldAddNewUser() {
        final RoleDto expectedRoleDto = createRoleDto();
        final Role expectedRole = createUserRole();
        final String name = expectedRoleDto.getName();
        when(roleRepository.existsByName(name)).thenReturn(false);

        final RoleDto actualRoleDto = roleService.addNewRole(expectedRoleDto);

        verify(roleRepository).existsByName(name);
        verify(roleRepository).save(refEq(expectedRole, "id"));
        assertThat(actualRoleDto).isEqualTo(expectedRoleDto);
    }

    @Test
    void shouldNotAddNewUser() {
        final RoleDto expectedRoleDto = createRoleDto();
        final Role expectedRole = createUserRole();
        when(roleRepository.existsByName(expectedRole.getName())).thenReturn(true);

        assertThatThrownBy(() -> roleService.addNewRole(expectedRoleDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ROLE_ALREADY_EXISTS);

        verify(roleRepository).existsByName(expectedRole.getName());
    }

    @Test
    void shouldDeleteUserByUsername() {
        final Role expectedRole = createUserRole();
        final String name = expectedRole.getName();
        when(roleRepository.findByName(name)).thenReturn(Optional.of(expectedRole));

        roleService.deleteByName(name);

        verify(roleRepository).findByName(name);
        verify(roleRepository).deleteByName(name);
    }

    @Test
    void shouldNotDeleteUserByUsername() {
        final String name = "unknown";
        when(roleRepository.findByName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.deleteByName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ROLE_NOT_FOUND);

        verify(roleRepository).findByName(name);
    }
}
