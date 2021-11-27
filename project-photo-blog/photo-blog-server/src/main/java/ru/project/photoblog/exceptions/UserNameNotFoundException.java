package ru.project.photoblog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNameNotFoundException  extends RuntimeException {
    public UserNameNotFoundException(String message) {
        super(message);
    }
}
