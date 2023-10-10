package com.example.MyStore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Picture not found!")
public class DefaultPictureNotFoundException extends RuntimeException {

    public DefaultPictureNotFoundException(String message) {
        super(message);
    }
}
