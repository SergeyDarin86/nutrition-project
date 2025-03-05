package ru.darin.nutrition_recommendation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.resource.NutritionResource;
import ru.darin.nutrition_recommendation.service.NutritionService;
import ru.darin.nutrition_recommendation.util.exception.ExceptionBuilder;

@RestController
@RequiredArgsConstructor
public class NutritionController implements NutritionResource {

    private final NutritionService nutritionService;

    @GetMapping("/getAllPeople")
    public ResponseEntity getAllPeople() {
        return ResponseEntity.ok(nutritionService.getAllPeople());
    }

    @PostMapping("/addPerson")
    public ResponseEntity addPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        nutritionService.addPerson(personDTO);
        return ResponseEntity.ok().build();
    }

}
