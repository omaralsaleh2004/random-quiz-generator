package com.omar.question_service.Exception;


import com.omar.question_service.dto.ErrorRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorRes> handleInvalidData(InvalidDataException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorRes error = new ErrorRes(ex.getMessage() , status.value());

        return new ResponseEntity<>(error , status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorRes> handleNotFound(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorRes error = new ErrorRes(ex.getMessage() , status.value());

        return new ResponseEntity<>(error , status);
    }


}
