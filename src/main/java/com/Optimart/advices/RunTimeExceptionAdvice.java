//package com.Optimart.advices;
//
//import com.Optimart.exceptions.CustomException;
//import com.Optimart.responses.ErrorResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.Date;
//
//@RestControllerAdvice
//public class RunTimeExceptionAdvice {
//
//    @ExceptionHandler(value = CustomException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                ex.getHttpStatus().value(),
//                new Date(),
//                ex.getMessage(),
//                request.getDescription(false)) {
//        };
//        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
//    }
//}