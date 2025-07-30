package org.capco.flexiprice.controller;

import org.capco.flexiprice.dto.ApiErrorDTO;
import org.capco.flexiprice.exception.CartNotFoundException;
import org.capco.flexiprice.exception.PersonalClientNotFoundException;
import org.capco.flexiprice.exception.ProductNotFoundException;
import org.capco.flexiprice.exception.ProductPriceNotFoundException;
import org.capco.flexiprice.exception.ProfessionalNotFoundException;
import org.capco.flexiprice.exception.SirenNumberAlreadyExistsException;
import org.capco.flexiprice.exception.UserNameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(SirenNumberAlreadyExistsException.class)
    public ResponseEntity<ApiErrorDTO> handleSirenNumberAlreadyExistsException(SirenNumberAlreadyExistsException e) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorDTO> handleUserNameAlreadyExistsException(UserNameAlreadyExistsException e) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleCartNotFoundException(CartNotFoundException e) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleProfessionalNotFoundException(ProfessionalNotFoundException e) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(PersonalClientNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handlePersonalClientNotFoundException(PersonalClientNotFoundException e) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleProductNotFoundException(ProductNotFoundException e) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(ProductPriceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleProductPriceNotFoundException(ProductPriceNotFoundException e) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
