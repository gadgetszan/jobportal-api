package com.gadgetszan.jobportal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JpBadRequestException extends RuntimeException{
    public JpBadRequestException(String message){
        super(message);
    }
}
