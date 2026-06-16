package com.parking.proj_parking_api.exception;

public class CodigoUniqueViolationException extends RuntimeException {

    public CodigoUniqueViolationException (String message) {
        super(message);
    }
}
