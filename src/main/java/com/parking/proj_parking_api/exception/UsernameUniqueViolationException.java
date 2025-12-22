package com.parking.proj_parking_api.exception;

public class UsernameUniqueViolationException extends RuntimeException {

    public UsernameUniqueViolationException (String message){
        super(message);
    }

}
