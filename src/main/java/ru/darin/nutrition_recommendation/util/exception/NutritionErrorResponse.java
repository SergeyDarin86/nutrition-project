package ru.darin.nutrition_recommendation.util.exception;

import lombok.Data;

@Data
public class NutritionErrorResponse {

    private String message;

    private long timestamp;

    public NutritionErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

}
