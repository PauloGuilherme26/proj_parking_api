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
import com.parking.proj_parking_api.exception.VagaDisponivelException;
import com.parking.proj_parking_api.exception.CodigoUniqueViolationException;
import com.parking.proj_parking_api.exception.CpfUniqueViolationException;
import com.parking.proj_parking_api.exception.EntityNotFoundException;
import com.parking.proj_parking_api.exception.PasswordInvalidException;
import com.parking.proj_parking_api.exception.ReciboCheckInNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice       //Ouvinte
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ReciboCheckInNotFoundException.class)            // Erro de usuário não encontrado!
    public ResponseEntity <ErrorMessage> reciboCheckInNotFoundException (   ReciboCheckInNotFoundException ex, 
                                                                            HttpServletRequest request) {
        Object[] params = new Object[]{ex.getRecibo()};
        String message = messageSource.getMessage("exception.reciboCheckInNotFoundException", params, request.getLocale());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
    }

    @ExceptionHandler(EntityNotFoundException.class)            // Erro de usuário não encontrado!
    public ResponseEntity <ErrorMessage> entityNotFoundException (  EntityNotFoundException ex, 
                                                                    HttpServletRequest request) {
        Object[] params = new Object[]{ex.getRecurso(), ex.getCodigo()};
        String message = messageSource.getMessage("exception.entityNotFoundException", params, request.getLocale());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
    }

    @ExceptionHandler( CodigoUniqueViolationException.class )   
    public ResponseEntity <ErrorMessage> codigoUniqueViolationException ( CodigoUniqueViolationException ex, 
                                                                            HttpServletRequest request) {
        Object[] params = new Object[]{ex.getRecurso(), ex.getCodigo()};
        String message = messageSource.getMessage("exception.codigoUniqueViolationException", params, request.getLocale());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.CONFLICT, message));
    }

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

    // Para @PathVariable / @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity <ErrorMessage> constraintViolationException (ConstraintViolationException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        String field = "";
        String message = "";

        // Extraimos os erros diretamente das "violações"
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String path = violation.getPropertyPath().toString();

            //Limpa o nome do método do caminho (ex: getByCodigo -> codigo)
            field = path.contains(".") ? path.substring(path.lastIndexOf(".") +1) : path;
            message = violation.getMessage();
        }
        
        return ResponseEntity.status(status).body(new ErrorMessage(request, status, field, message));
    }

    @ExceptionHandler(
        {   UsernameUniqueViolationException.class,         // Erro de usuário já cadastrado!
            CpfUniqueViolationException.class }   )   
    public ResponseEntity <ErrorMessage> uniqueViolationException ( RuntimeException ex, 
                                                                    HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
    }  

     @ExceptionHandler(VagaDisponivelException.class)            
    public ResponseEntity <ErrorMessage> vagaDisponivelException (  RuntimeException ex, 
                                                                    HttpServletRequest request) {
        String message = messageSource.getMessage("exception.vagaDisponivelException", null, request.getLocale());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
    }

    @ExceptionHandler(PasswordInvalidException.class)            // Erro de senha não confere!
    public ResponseEntity <ErrorMessage> passwordInvalidException ( RuntimeException ex, 
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
