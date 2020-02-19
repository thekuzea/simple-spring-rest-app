package com.thekuzea.experimental.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.thekuzea.experimental.api.dto.error.ErrorResult;
import com.thekuzea.experimental.support.constant.messages.process.AuthenticationMessages;

@Slf4j
@RestControllerAdvice
public class ExceptionMapper {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResult handleException(final MethodArgumentNotValidException e) {
        final ErrorResult errorResult = new ErrorResult();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                        errorResult.addError(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                );

        return errorResult;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult handleException(final IllegalArgumentException e) {
        return createResponseWithMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResult handleAuthenticationException() {
        return createResponseWithMessage(AuthenticationMessages.AUTHENTICATION_FAILED);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResult handleGenericException(final Exception exception) {
        log.error("Error occurred: ", exception);
        return createResponseWithMessage("Unexpected error");
    }

    private static ErrorResult createResponseWithMessage(final String message) {
        final ErrorResult errorResult = new ErrorResult();
        errorResult.addError(null, message);

        return errorResult;
    }
}
