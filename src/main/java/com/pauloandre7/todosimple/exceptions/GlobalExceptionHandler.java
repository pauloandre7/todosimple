package com.pauloandre7.todosimple.exceptions;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pauloandre7.todosimple.services.exceptions.DataBindingViolationException;
import com.pauloandre7.todosimple.services.exceptions.ObjectNotFoundException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
    
    @Value("${server.error.include-exception}")
    private boolean printStackTrace;

    
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
                MethodArgumentNotValidException ex,
                HttpHeaders headers,
                HttpStatus status,
                WebRequest request){

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(), 
            "Validation error. Check 'errors' field for details");
            
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
        ObjectNotFoundException ex, WebRequest request
    ) {
        log.error("Faild to find the requested element", ex);
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DataBindingViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolationException(
        DataBindingViolationException ex, WebRequest request
    ) {
        log.error("Faild to save entity with associated data", ex);
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception, WebRequest request ) {
            final String errorMessage = "Unknown error occurred";
            log.error(errorMessage, exception);
            return buildErrorResponse(exception, errorMessage, 
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    private ResponseEntity<Object> buildErrorResponse(
        Exception exception, HttpStatus httpStatus, WebRequest request){

        // Utiliza o build de 4 parâmetros para construir com mensagem da exception
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
        Exception exception, String message, HttpStatus httpStatus, WebRequest request){
        
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        
        if(this.printStackTrace){
            // O ExceptionUtils vai pegar a exeção e setar no objeto de ErrorResponse que criamos
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }

        // Retorna o error devidamente montado com o status e o body correto.
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
        DataIntegrityViolationException ex, WebRequest request
    ){
        String errorMessage = ex.getMostSpecificCause().getMessage();
        log.error("Failed to save entity with integrity problmes: " + errorMessage, ex);

        return buildErrorResponse(ex, errorMessage, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolationException(
        ConstraintViolationException ex, WebRequest request 
        ){
        log.error("Failed to validate element", ex);

        return buildErrorResponse(ex, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
