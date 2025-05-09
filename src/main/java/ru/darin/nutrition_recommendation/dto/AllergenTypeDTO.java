package ru.darin.nutrition_recommendation.dto;

import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.UUID;

@Data
@NutritionValidation
public class AllergenTypeDTO {

    public AllergenTypeDTO(UUID allergenId, String allergenTitle) {
        this.allergenId = allergenId;
        this.allergenTitle = allergenTitle;
    }

    private UUID allergenId;

    private String allergenTitle;

    private String titleColor;

}