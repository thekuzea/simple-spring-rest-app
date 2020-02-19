package com.thekuzea.experimental.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.thekuzea.experimental.api.dto.UserDto;
import com.thekuzea.experimental.api.dto.validation.NotBlankRequired;
import com.thekuzea.experimental.api.dto.validation.SizeRequired;

@RequestMapping("/user")
public interface UserController {

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<UserDto> getAllUsers();

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto getByUsername(@PathVariable("username") String username);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserDto addNewUser(@Validated({NotBlankRequired.class, SizeRequired.class}) @RequestBody UserDto dto);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserDto updateByUserId(@PathVariable("userId") String userId, @Validated(SizeRequired.class) @RequestBody UserDto dto);

    @PreAuthorize("hasAuthority('admin')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/{username}")
    void deleteByUsername(@PathVariable("username") String username);
}
