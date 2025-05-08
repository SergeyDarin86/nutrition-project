package ru.darin.nutrition_recommendation.util;

import lombok.Data;
import ru.darin.nutrition_recommendation.dto.ProductDTO;

import java.util.List;
import java.util.Map;

@Data
public class RecommendationResponseWithDTO {

    private String protocol;

    private String resolution;

    private List<Map<String, List<ProductDTO>>> products;

}
