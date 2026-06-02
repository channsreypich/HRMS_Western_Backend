package hrd.com.hrms.exception;

import hrd.com.hrms.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catch 404 Not Found Exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                ex.getMessage(),               // 1. message (String)
                null,                          // 2. data (Object)
                HttpStatus.NOT_FOUND.value()   // 3. status (int)
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Catch 400 Bad Request Exceptions
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(BadRequestException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                ex.getMessage(),
                null,
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Catch 401 Unauthorized Exceptions
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(UnauthorizedException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                ex.getMessage(),
                null,
                HttpStatus.UNAUTHORIZED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle JSR-383 Validation Errors (e.g., @NotBlank, @Min annotations on request DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                "Validation failed.",               // 1. message (String)
                errors,                             // 2. data (Map)
                HttpStatus.BAD_REQUEST.value()      // 3. status (int)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Catch-All fallback for any unhandled internal server errors (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                "An unexpected internal server error occurred.", // 1. message (String)
                null,                                             // 2. data (Object)
                HttpStatus.INTERNAL_SERVER_ERROR.value()          // 3. status (int)
        );
        // Print the log stack trace in the terminal so you can debug what happened
        ex.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}