package ru.darin.nutrition_recommendation.dto;

import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.List;
import java.util.UUID;

@Data
@NutritionValidation
public class ProductDTO implements Comparable<ProductDTO> {

    private UUID productId;

    private String product;

    private ProductTypeDTO productTypeDTO;

    private List<AllergenTypeDTO> allergenTypes;

    @Override
    public int compareTo(ProductDTO o) {
        return this.getProduct().compareTo(o.getProduct());
    }

}