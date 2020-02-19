package com.thekuzea.experimental.api.dto.error;

import lombok.Value;

@Value
public class ValidationError {

    String source;

    String details;
}
