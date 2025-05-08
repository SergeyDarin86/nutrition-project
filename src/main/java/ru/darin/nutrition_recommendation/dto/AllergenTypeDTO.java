package ru.darin.nutrition_recommendation.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AllergenTypeDTO {

    public AllergenTypeDTO(UUID allergenId, String allergenTitle) {
        this.allergenId = allergenId;
        this.allergenTitle = allergenTitle;
    }

    private UUID allergenId;

    private String allergenTitle;

    private String titleColor;

}