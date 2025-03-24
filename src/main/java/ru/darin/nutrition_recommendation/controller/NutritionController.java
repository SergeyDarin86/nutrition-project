package ru.darin.nutrition_recommendation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.darin.nutrition_recommendation.dto.*;
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

    @PostMapping("/addProductType")
    public ResponseEntity addProductType(@RequestBody @Valid ProductTypeDTO productTypeDTO, BindingResult bindingResult){
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        return ResponseEntity.ok(nutritionService.addProductType(productTypeDTO));
    }

    @DeleteMapping("/deleteProductTypeById/{id}")
    public ResponseEntity deleteProductTypeById(@PathVariable("id") UUID id){
        nutritionService.deleteProductTypeById(id);
        return ResponseEntity.ok().body("Тип продукта удален из списка успешно");
    }

    @PatchMapping("/updateProductTypeById/{id}")
    public ResponseEntity updateProductTypeById(@RequestBody @Valid ProductTypeDTO productTypeDTO, BindingResult bindingResult,
                                            @PathVariable("id") UUID id) {
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        return ResponseEntity.ofNullable(nutritionService.updateProductTypeById(id, productTypeDTO));
    }

    @GetMapping("/getAllProductTypes")
    public ResponseEntity getAllProductTypes(){
        return ResponseEntity.ok(nutritionService.getAllProductTypes());
    }

    @PostMapping("/addProduct")
    public ResponseEntity addProduct(@RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult){
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        return ResponseEntity.ok(nutritionService.addProduct(productDTO));
    }

    @DeleteMapping("/deleteProductById/{id}")
    public ResponseEntity deleteProductById(@PathVariable("id") UUID id){
        nutritionService.deleteProductById(id);
        return ResponseEntity.ok().body("Продукт удален из списка успешно");
    }

    @PatchMapping("/updateProductById/{id}")
    public ResponseEntity updateProductById(@RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult,
                                                @PathVariable("id") UUID id) {
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        return ResponseEntity.ofNullable(nutritionService.updateProductById(id, productDTO));
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity getAllProducts(){
        return ResponseEntity.ok(nutritionService.getAllProducts());
    }

    @GetMapping("/getIllnessWithProducts")
    public ResponseEntity getAllIllnessWithProductsGroupedByType(
            @RequestParam(value = "illness") String illness,
            @RequestParam(value = "resolution") String resolution){
        return ResponseEntity.ok(nutritionService.getIllnessWithProductsGroupedByType(illness, resolution));
    }

    @PostMapping("/addMix")
    public ResponseEntity addMixOfProductsAndIllnesses(@RequestBody @Valid MixDTO mixDTO, BindingResult bindingResult){
        ExceptionBuilder.buildErrorMessageForClient(bindingResult);
        nutritionService.addMixOfProductsAndIllnesses(mixDTO);
        return ResponseEntity.ok().build();
    }

}