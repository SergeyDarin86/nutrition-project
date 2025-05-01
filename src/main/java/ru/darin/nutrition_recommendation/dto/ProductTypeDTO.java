package ru.darin.nutrition_recommendation.dto;

import lombok.Data;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NutritionValidation
public class ProductTypeDTO {

    private UUID productTypeId;

    private String productType;

    private List<ProductDTO> products;

    public List<ProductDTO> getProducts() {
        return this.products != null ? this.products.stream().sorted().toList() : new ArrayList<>();
    }

}
