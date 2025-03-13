package ru.darin.nutrition_recommendation.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private String product;

    private ProductTypeDTO productTypeDTO;

}