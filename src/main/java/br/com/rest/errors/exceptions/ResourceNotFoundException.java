package br.com.rest.errors.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    
    private String message;

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }  
    

}
