package com.geeksforless.client.controller.advice;

import com.geeksforless.client.exception.ScenarioNotFoundException;
import com.geeksforless.client.exception.UsernameIsAlreadyExist;
import com.geeksforless.client.exception.error.ApiError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private static ApiError badRequestApiError(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return apiError;
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponseEntity(badRequestApiError(ex));
    }

    @ExceptionHandler(value = {UsernameIsAlreadyExist.class})
    public ResponseEntity<Object> handleUsernameIsAlreadyExist(UsernameIsAlreadyExist ex) {
        return buildResponseEntity(badRequestApiError(ex));
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildResponseEntity(badRequestApiError(ex));
    }

    @ExceptionHandler(value = {ScenarioNotFoundException.class})
    public ResponseEntity<Object> handleScenarioNotFoundException(ScenarioNotFoundException ex) {
        return buildResponseEntity(badRequestApiError(ex));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildResponseEntity(badRequestApiError(ex));
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        return buildResponseEntity(badRequestApiError(ex));
    }
}
