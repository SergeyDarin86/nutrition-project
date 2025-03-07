package ru.darin.nutrition_recommendation.dto;

import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.List;

@Data
@NutritionValidation
public class PersonDTO {

    private String fullName;

    private List<IllnessDTO> illnesses;

}
