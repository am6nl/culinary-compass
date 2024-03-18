package nl.abnamro.culinarycompass.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex){

        log.error(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder().message(ex.getMessage()).build(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        log.error(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder().message(ex.getMessage()).build(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){

        ex.printStackTrace();
        log.error("Exception: {}",ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder().message("Internal server error").build(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
