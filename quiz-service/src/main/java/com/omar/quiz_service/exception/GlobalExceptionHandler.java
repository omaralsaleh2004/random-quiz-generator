package com.omar.quiz_service.exception;


import com.omar.quiz_service.dto.ErrorRes;
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

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorRes> handleUnAuthorizedException(UnAuthorizedException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        ErrorRes error = new ErrorRes(ex.getMessage() , status.value());

        return new ResponseEntity<>(error , status);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorRes> handleInvalidDateException(InvalidDateException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorRes error = new ErrorRes(ex.getMessage() , status.value());

        return new ResponseEntity<>(error , status);
    }

    @ExceptionHandler(InvalidTimeException.class)
    public ResponseEntity<ErrorRes> handleInvalidTimeException(InvalidTimeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorRes error = new ErrorRes(ex.getMessage() , status.value());

        return new ResponseEntity<>(error , status);
    }


}
