package com.ct4health.t4health.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ApiError buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Handles @Valid validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");

        ApiError error = buildError(HttpStatus.BAD_REQUEST, msg, request);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom "not found" exception
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiError> handleCustomerNotFound(
            CustomerNotFoundException ex,
            HttpServletRequest request) {

        ApiError error = buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles IllegalArgumentException (duplicates, invalid input)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArg(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ApiError error = buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ConstraintViolationException (path variables, etc.)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        ApiError error = buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all: any unhandled runtime exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralExceptions(
            Exception ex,
            HttpServletRequest request) {

        ApiError error = buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request);

        ex.printStackTrace(); // log for debugging

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
