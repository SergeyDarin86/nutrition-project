package ru.darin.nutrition_recommendation.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler({NutritionException.class})
    public ResponseEntity<NutritionErrorResponse> handlerException(NutritionException e) {
        NutritionErrorResponse response = new NutritionErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
