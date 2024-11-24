package oBeta.PiggyWebBank.exceptions;

import oBeta.PiggyWebBank.payloads.ErrorsResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponseDTO handleBadrequest(BadRequestException ex) {
        return new ErrorsResponseDTO("400", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsResponseDTO handleBadJSON(HttpMessageNotReadableException ex){
        return new ErrorsResponseDTO("500", "Error deserializing the JSON", LocalDateTime.now());
    }

//    @ExceptionHandler(UnauthorizedException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ErrorsResponseDTO handleUnauthorized(UnauthorizedException ex) {
//        return new ErrorsResponseDTO("401", ex.getMessage(), LocalDateTime.now());
//    }

//    @ExceptionHandler(AuthorizationDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ErrorsResponseDTO handleForbidden(AuthorizationDeniedException ex) {
//        return new ErrorsResponseDTO("403", "Authorization Denied", LocalDateTime.now());
//    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsResponseDTO handleNotFound(NotFoundException ex) {
        return new ErrorsResponseDTO("404", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsResponseDTO handleGeneric(Exception ex) {
        ex.printStackTrace();
        return new ErrorsResponseDTO("500", "Internal server error!", LocalDateTime.now());
    }

}
