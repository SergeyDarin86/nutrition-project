package ru.darin.nutrition_recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class NutritionRecommendationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutritionRecommendationApplication.class, args);
	}

}