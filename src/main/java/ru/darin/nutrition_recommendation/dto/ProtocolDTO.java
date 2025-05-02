package ru.darin.nutrition_recommendation.dto;

import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.UUID;

@Data
@NutritionValidation
public class ProtocolDTO {

    private UUID protocolId;

    private String protocolTitle;

}