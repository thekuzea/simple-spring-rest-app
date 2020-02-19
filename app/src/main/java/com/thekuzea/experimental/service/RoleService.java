package com.thekuzea.experimental.service;

import java.util.List;

import com.thekuzea.experimental.api.dto.RoleDto;

public interface RoleService {

    List<RoleDto> getAllRoles();

    RoleDto addNewRole(RoleDto dto);

    void deleteByName(String name);
}
