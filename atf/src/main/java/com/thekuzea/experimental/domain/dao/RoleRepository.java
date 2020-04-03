package com.thekuzea.experimental.domain.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.RepositoryDefinition;

import com.thekuzea.experimental.domain.model.Role;

@RepositoryDefinition(domainClass = Role.class, idClass = UUID.class)
public interface RoleRepository {

    Optional<Role> findRoleByName(String name);
}
