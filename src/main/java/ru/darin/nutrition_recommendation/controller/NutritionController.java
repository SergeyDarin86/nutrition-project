package ru.darin.nutrition_recommendation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NutritionController {

    @GetMapping("/getMessage")
    public ResponseEntity getMessage(){
        return ResponseEntity.ok("hello");
    }

}
