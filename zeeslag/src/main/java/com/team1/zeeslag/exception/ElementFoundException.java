package com.team1.zeeslag.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ElementFoundException extends RuntimeException {

    public ElementFoundException(String message) {
        super(message);
    }


}
