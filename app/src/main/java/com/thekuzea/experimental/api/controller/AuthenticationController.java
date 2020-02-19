package com.thekuzea.experimental.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.thekuzea.experimental.api.dto.TokenDto;
import com.thekuzea.experimental.api.dto.UserDto;
import com.thekuzea.experimental.api.dto.validation.NotBlankRequired;
import com.thekuzea.experimental.api.dto.validation.SizeRequired;

@RequestMapping("/auth")
public interface AuthenticationController {

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    TokenDto authenticate(@Validated({NotBlankRequired.class, SizeRequired.class}) @RequestBody UserDto dto);
}
