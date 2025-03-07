package ru.darin.nutrition_recommendation.dto;

import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

@Data
@NutritionValidation
public class IllnessDTO {

    private String illnessTitle;

}
