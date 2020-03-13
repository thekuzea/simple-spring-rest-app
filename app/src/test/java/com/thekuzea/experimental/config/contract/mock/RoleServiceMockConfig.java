package com.thekuzea.experimental.config.contract.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.thekuzea.experimental.service.RoleService;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.ROLE_ALREADY_EXISTS;
import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.test.util.dto.RoleDtoTestDataGenerator.createRoleDtoAsNewRole;
import static com.thekuzea.experimental.test.util.dto.RoleDtoTestDataGenerator.createRoleDtoAsNewRoleResponse;
import static com.thekuzea.experimental.test.util.dto.RoleDtoTestDataGenerator.createRoleDtoList;
import static com.thekuzea.experimental.test.util.dto.RoleDtoTestDataGenerator.createRoleDtoNegativeFlow;

@TestConfiguration
@Profile("contract")
public class RoleServiceMockConfig {

    @Bean
    @Primary
    public RoleService roleService() {
        final RoleService roleService = mock(RoleService.class);

        when(roleService.getAllRoles()).thenReturn(createRoleDtoList());
        doThrow(new IllegalArgumentException(ROLE_NOT_FOUND)).when(roleService).deleteByName("moderator");

        when(roleService.addNewRole(refEq(createRoleDtoAsNewRole())))
                .thenReturn(createRoleDtoAsNewRoleResponse());
        when(roleService.addNewRole(refEq(createRoleDtoNegativeFlow())))
                .thenThrow(new IllegalArgumentException(ROLE_ALREADY_EXISTS));

        return roleService;
    }
}
