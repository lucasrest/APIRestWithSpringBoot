package br.com.rest.handlers;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import br.com.rest.errors.details.ViolationDetails;
import br.com.rest.errors.details.ErrorDefaultDetails;
import br.com.rest.errors.details.ResourceNotFoundDetails;
import br.com.rest.errors.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handlerResourceNotFoundException(ResourceNotFoundException rnfException){
        
        ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder
                                                        .newBuilder()
                                                        .timestamp(new Date().getTime())
                                                        .status(HttpStatus.NOT_FOUND.value())
                                                        .title("Resource not found")
                                                        .developerMessage(rnfException.getClass().getSimpleName())
                                                        .build();                                        
        return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
    }
    
    @Override    
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException,
                                                               HttpHeaders headers, 
                                                               HttpStatus status, 
                                                               WebRequest request ){
        //System.out.println(manvException.getBindingResult().getFieldErrors());
        ViolationDetails vDetails = ViolationDetails.Builder
                                                        .newBuilder()
                                                        .timestamp(new Date().getTime())
                                                        .status(HttpStatus.BAD_REQUEST.value())
                                                        .title("Erro ao validar os Campos")
                                                        .developerMessage(manvException.getClass().getSimpleName())
                                                        .build();                                        
        return new ResponseEntity<>(vDetails, HttpStatus.BAD_REQUEST);
    }    
    
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, 
                                                          Object body,
                                                          HttpHeaders headers, 
                                                          HttpStatus status, 
                                                          WebRequest request) {
        ErrorDefaultDetails errorDetail = ErrorDefaultDetails.Builder
                                                                .newBuilder()
                                                                .timestamp(new Date().getTime())
                                                                .status(status.value())
                                                                .title("Internal Exception")
                                                                .detail(ex.getMessage())
                                                                .developerMessage(ex.getClass().getName())
                                                                .build();                                        
        return new ResponseEntity<>(errorDetail, headers, status);
    }
    
}
