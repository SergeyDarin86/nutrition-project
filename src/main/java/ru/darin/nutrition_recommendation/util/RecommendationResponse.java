package ru.darin.nutrition_recommendation.util;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RecommendationResponse {

    private String protocol;

    private String resolution;

    private List<Map<String,List<String>>> products;

}