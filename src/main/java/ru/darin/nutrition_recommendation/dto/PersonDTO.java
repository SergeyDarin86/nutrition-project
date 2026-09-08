package ru.darin.nutrition_recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.List;
import java.util.UUID;

@Data
@NutritionValidation
@Schema(description = "Класс, для ввода/отображения данных о человеке.")
public class PersonDTO {

    private UUID personId;

    @JsonProperty("fullName")
    @Schema(description = "ФИО", example = "Иванов Андрей Михайлович")
    private String fullName;

    @JsonProperty("protocols")
    @Schema(description = "Протоколы")
    private List<ProtocolDTO> protocols;

}