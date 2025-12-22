package com.parking.proj_parking_api.exception;

public class PasswordInvalidException extends RuntimeException {
    
    public PasswordInvalidException (String message){
        super(message);
    }
}
