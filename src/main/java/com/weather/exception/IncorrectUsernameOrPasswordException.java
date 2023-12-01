package com.weather.exception;

public class IncorrectUsernameOrPasswordException extends RuntimeException {

    public IncorrectUsernameOrPasswordException(String messgae) {
        super(messgae);
    }

}
