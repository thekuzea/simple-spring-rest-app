package com.thekuzea.experimental.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thekuzea.experimental.support.constant.messages.entity.RoleMessages;
import com.thekuzea.experimental.support.constant.messages.logging.LoggingMessages;
import com.thekuzea.experimental.infrastructure.persistence.RoleRepository;
import com.thekuzea.experimental.api.dto.RoleDto;
import com.thekuzea.experimental.domain.mapper.RoleMapper;
import com.thekuzea.experimental.domain.model.Role;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDto addNewRole(final RoleDto dto) {
        final boolean roleExists = roleRepository.existsByName(dto.getName());

        if (!roleExists) {
            final Role mappedRole = RoleMapper.mapToModel(dto);
            roleRepository.save(mappedRole);

            log.debug(LoggingMessages.SAVED_NEW_ROLE, mappedRole.getName());
        } else {
            log.debug(LoggingMessages.ROLE_ALREADY_EXISTS_LOG, dto.getName());
            throw new IllegalArgumentException(RoleMessages.ROLE_ALREADY_EXISTS);
        }

        return dto;
    }

    @Override
    @Transactional
    public void deleteByName(final String name) {
        final Optional<Role> possibleRole = roleRepository.findByName(name);

        if (possibleRole.isPresent()) {
            roleRepository.deleteByName(name);
            log.debug(LoggingMessages.DELETED_ROLE, name);
        } else {
            log.debug(LoggingMessages.ROLE_NOT_FOUND_BY_NAME, name);
            throw new IllegalArgumentException(RoleMessages.ROLE_NOT_FOUND);
        }
    }
}
