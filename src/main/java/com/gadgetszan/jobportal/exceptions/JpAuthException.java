package com.gadgetszan.jobportal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JpAuthException extends RuntimeException {
    public JpAuthException(String message){
        super(message);
    }
}
