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

    @PatchMapping("addIllnessToPerson/{id}")
    public ResponseEntity addIllnessToPersonById(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult,
                                                 @PathVariable("id") UUID id) {
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        return ResponseEntity.ofNullable(nutritionService.addIllnessToPerson(id, personDTO));
    }

    @PatchMapping("curePerson/{id}")
    public ResponseEntity curePersonById(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult,
                                                 @PathVariable("id") UUID id) {
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        return ResponseEntity.ofNullable(nutritionService.curePerson(id, personDTO));
    }

    @PatchMapping("updatePersonById/{id}")
    public ResponseEntity updatePersonById(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult,
                                           @PathVariable("id") UUID id) {
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        return ResponseEntity.ofNullable(nutritionService.updatePersonById(id, personDTO));
    }

    @DeleteMapping("/deletePersonById/{id}")
    public ResponseEntity deletePersonById(@PathVariable("id") UUID id){
        nutritionService.deletePersonById(id);
        return ResponseEntity.ok().body("Пользователь удален успешно");
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

    @PatchMapping("/updateIllnessById/{id}")
    public ResponseEntity updateIllnessById(@RequestBody @Valid IllnessDTO illnessDTO, BindingResult bindingResult,
                                           @PathVariable("id") UUID id) {
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        return ResponseEntity.ofNullable(nutritionService.updateIllnessById(id, illnessDTO));
    }

    @DeleteMapping("/deleteIllnessById/{id}")
    public ResponseEntity deleteIllnessById(@PathVariable("id") UUID id){
        nutritionService.deleteIllnessById(id);
        return ResponseEntity.ok().body("Заболевание удалено из списка успешно");
    }

}