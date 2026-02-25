package com.parking.proj_parking_api.exception;

public class CpfUniqueViolationException extends RuntimeException {

    public CpfUniqueViolationException (String message) {
        super(message);
    }

}
