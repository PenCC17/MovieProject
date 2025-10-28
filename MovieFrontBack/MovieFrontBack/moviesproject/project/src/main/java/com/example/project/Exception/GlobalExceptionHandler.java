// package com.example.project.Exception;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// // import jakarta.validation.ConstraintViolationException;

// // @RestControllerAdvice
// // public class GlobalExceptionHandler {

// //     // @ExceptionHandler("Exception.class")
// //     // public ResponseEntity<String> 
// //     @ExceptionHandler(ConstraintViolationException.class)
// //     public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
// //         Map<String, String> errors = new HashMap<>();
// //         ex.getConstraintViolations().forEach(cv ->
// //                 errors.put(cv.getPropertyPath().toString(), cv.getMessage()));
// //         return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
// //     }
// // }
