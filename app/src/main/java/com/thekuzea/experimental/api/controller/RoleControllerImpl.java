package com.thekuzea.experimental.api.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import com.thekuzea.experimental.api.dto.RoleDto;
import com.thekuzea.experimental.service.RoleService;

@RestController
@RequiredArgsConstructor
public class RoleControllerImpl implements RoleController {

    private final RoleService roleService;

    @Override
    public List<RoleDto> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Override
    public RoleDto addNewRole(final RoleDto dto) {
        return roleService.addNewRole(dto);
    }

    @Override
    public void deleteByName(final String name) {
        roleService.deleteByName(name);
    }
}
