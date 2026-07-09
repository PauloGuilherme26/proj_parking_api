package com.parking.proj_parking_api.web.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.parking.proj_parking_api.exception.UsernameUniqueViolationException;
import com.parking.proj_parking_api.exception.CodigoUniqueViolationException;
import com.parking.proj_parking_api.exception.CpfUniqueViolationException;
import com.parking.proj_parking_api.exception.EntityNotFoundException;
import com.parking.proj_parking_api.exception.PasswordInvalidException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice       //Ouvinte
public class ApiExceptionHandler {

    private final MessageSource messageSource;

     @ExceptionHandler(AccessDeniedException.class)            // Erro de acesso negado!
    public ResponseEntity <ErrorMessage> accessDeniedException ( AccessDeniedException ex, 
                                                                    HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)        // Erro de campo inválido!
    public ResponseEntity <ErrorMessage> methodArgumentNotValidException ( MethodArgumentNotValidException ex, 
                                                                           HttpServletRequest request, 
                                                                           BindingResult result ) {
        log.error("Api Error - ", ex);
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(
                request, 
                HttpStatus.UNPROCESSABLE_ENTITY, 
                messageSource.getMessage("message.invalid.field", null, request.getLocale()), 
                result, 
                messageSource)
            );
    }

    @ExceptionHandler(
        {   UsernameUniqueViolationException.class,         // Erro de usuário já cadastrado!
            CpfUniqueViolationException.class, 
            CodigoUniqueViolationException.class    }   )   
    public ResponseEntity <ErrorMessage> uniqueViolationException ( RuntimeException ex, 
                                                                    HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)            // Erro de usuário não encontrado!
    public ResponseEntity <ErrorMessage> EntityNotFoundException (  RuntimeException ex, 
                                                                    HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(PasswordInvalidException.class)            // Erro de senha não confere!
    public ResponseEntity <ErrorMessage> PasswordInvalidException ( RuntimeException ex, 
                                                                    HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)                         // Erro que não for nenhum dos anteriores
    public ResponseEntity <ErrorMessage> internalServerErrorException ( Exception ex, HttpServletRequest request ) {
        ErrorMessage error = new ErrorMessage(
            request, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        log.error("Internal Server Error {} {} ", error, ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(error);
    }
}
