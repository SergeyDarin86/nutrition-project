package ru.darin.nutrition_recommendation.dto;

import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.List;
import java.util.UUID;

@Data
@NutritionValidation
public class PersonDTO {

    private UUID personId;

    private String fullName;

    private List<ProtocolDTO> protocols;

}