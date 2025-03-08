package ru.darin.nutrition_recommendation.resource;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.darin.nutrition_recommendation.dto.IllnessDTO;
import ru.darin.nutrition_recommendation.dto.PersonDTO;

import java.util.UUID;

public interface NutritionResource {

    ResponseEntity getAllPeople();

    ResponseEntity addPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult);

    ResponseEntity getPersonById(@PathVariable("id") UUID id);

    ResponseEntity updatePersonById(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, @PathVariable("id") UUID id);

    ResponseEntity addIllnessToPersonById(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, @PathVariable("id") UUID id);

    ResponseEntity getAllIllnesses();

    ResponseEntity addIllness(@RequestBody @Valid IllnessDTO illnessDTO, BindingResult bindingResult);

    ResponseEntity updateIllnessById(@RequestBody @Valid IllnessDTO illnessDTO, BindingResult bindingResult, @PathVariable("id") UUID id);

}
