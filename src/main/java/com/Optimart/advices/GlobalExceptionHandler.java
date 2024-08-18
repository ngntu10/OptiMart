package com.Optimart.advices;

import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.exceptions.FileUploadException;
import com.Optimart.exceptions.InvalidParamException;
import com.Optimart.responses.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = FileUploadException.class)
    public ResponseEntity<BaseResponse> handleFileUploadExceptions(FileUploadException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse(LocalDate.now(), ex.getMessage()));
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<BaseResponse> handleDataNotFoundExceptionExceptions(DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse(LocalDate.now(), ex.getMessage()));
    }

    @ExceptionHandler(value = InvalidParamException.class)
    public ResponseEntity<BaseResponse> handleInvalidParamException(InvalidParamException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse(LocalDate.now(), ex.getMessage()));
    }
}
