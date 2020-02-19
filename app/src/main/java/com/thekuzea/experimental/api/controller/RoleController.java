package com.thekuzea.experimental.api.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.thekuzea.experimental.api.dto.RoleDto;

@PreAuthorize("hasAuthority('admin')")
@RequestMapping("/role")
public interface RoleController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<RoleDto> getAllRoles();

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    RoleDto addNewRole(@Valid @RequestBody RoleDto dto);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/{name}")
    void deleteByName(@PathVariable("name") String name);
}
