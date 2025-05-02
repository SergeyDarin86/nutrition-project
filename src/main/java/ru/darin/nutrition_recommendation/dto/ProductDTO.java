package ru.darin.nutrition_recommendation.dto;

import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.UUID;

@Data
@NutritionValidation
public class ProductDTO implements Comparable<ProductDTO> {

    private UUID productId;

    private String product;

    private ProductTypeDTO productTypeDTO;

    @Override
    public int compareTo(ProductDTO o) {
        return this.getProduct().compareTo(o.getProduct());
    }

}