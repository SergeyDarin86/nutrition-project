package ru.darin.nutrition_recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.UUID;

@Data
@NutritionValidation
@Schema(description = "Класс, для ввода/отображения данных о протоколе питания.")
public class ProtocolDTO {

    private UUID protocolId;

    @JsonProperty("protocolTitle")
    @Schema(description = "Название протокола", example = "ЭРД")
    private String protocolTitle;

}