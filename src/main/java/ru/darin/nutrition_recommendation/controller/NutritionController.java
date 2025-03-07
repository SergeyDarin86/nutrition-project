package ru.darin.nutrition_recommendation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.darin.nutrition_recommendation.dto.IllnessDTO;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.resource.NutritionResource;
import ru.darin.nutrition_recommendation.service.NutritionService;
import ru.darin.nutrition_recommendation.util.exception.ExceptionBuilder;

import java.util.UUID;

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
        return ResponseEntity.ok(nutritionService.addPerson(personDTO));
    }

    @GetMapping("getById/{id}")
    public ResponseEntity getPersonById(@PathVariable("id") UUID id) {
        return ResponseEntity.ofNullable(nutritionService.getPersonById(id));
    }

    @GetMapping("/getAllIllnesses")
    public ResponseEntity getAllIllnesses() {
        return ResponseEntity.ok(nutritionService.getAllIllnesses());
    }

    @PostMapping("/addIllness")
    public ResponseEntity addIllness(@RequestBody @Valid IllnessDTO illnessDTO, BindingResult bindingResult) {
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        return ResponseEntity.ok(nutritionService.addIllness(illnessDTO));
    }

}
