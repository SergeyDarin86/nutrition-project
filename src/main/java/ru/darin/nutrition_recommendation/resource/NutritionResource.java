package ru.darin.nutrition_recommendation.resource;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import ru.darin.nutrition_recommendation.dto.IllnessDTO;
import ru.darin.nutrition_recommendation.dto.PersonDTO;

public interface NutritionResource {

    ResponseEntity getAllPeople();

    ResponseEntity addPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult);

    ResponseEntity getAllIllnesses();

    ResponseEntity addIllness(@RequestBody @Valid IllnessDTO illnessDTO, BindingResult bindingResult);

}
