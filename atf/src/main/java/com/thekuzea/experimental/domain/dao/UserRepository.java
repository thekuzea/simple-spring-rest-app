package com.thekuzea.experimental.domain.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.RepositoryDefinition;

import com.thekuzea.experimental.domain.model.User;

@RepositoryDefinition(domainClass = User.class, idClass = UUID.class)
public interface UserRepository {

    Optional<User> findUserByUsername(String username);
}
