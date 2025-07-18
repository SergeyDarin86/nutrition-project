package ru.darin.nutrition_recommendation.resource;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.darin.nutrition_recommendation.dto.ProtocolDTO;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.dto.ProductDTO;
import ru.darin.nutrition_recommendation.dto.ProductTypeDTO;

import java.util.UUID;

public interface NutritionResource {

    String getCommonPage();

//    ResponseEntity getAllPeople();
//
//    ResponseEntity addPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult);
//
//    ResponseEntity getPersonById(@PathVariable("id") UUID id);
//
//    ResponseEntity updatePersonById(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, @PathVariable("id") UUID id);
//
//    ResponseEntity addIllnessToPersonById(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, @PathVariable("id") UUID id);
//
//    ResponseEntity curePersonById(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, @PathVariable("id") UUID id);
//
//    ResponseEntity deletePersonById(@PathVariable("id") UUID id);
//
//    ResponseEntity getAllProtocols();
//
//    ResponseEntity addProtocol(@RequestBody @Valid ProtocolDTO protocolDTO, BindingResult bindingResult);
//
//    ResponseEntity updateProtocolById(@RequestBody @Valid ProtocolDTO protocolDTO, BindingResult bindingResult, @PathVariable("id") UUID id);
//
//    ResponseEntity deleteProtocolById(@PathVariable("id") UUID id);
//
//    ResponseEntity addProductType(@RequestBody @Valid ProductTypeDTO productTypeDTO, BindingResult bindingResult);
//
//    ResponseEntity deleteProductTypeById(@PathVariable("id") UUID id);
//
//    ResponseEntity getAllProductTypes();
//
//    ResponseEntity addProduct(@RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult);
//
//    ResponseEntity deleteProductById(@PathVariable("id") UUID id);
//
//    ResponseEntity updateProductTypeById(@RequestBody @Valid ProductTypeDTO productTypeDTO, BindingResult bindingResult,
//                                         @PathVariable("id") UUID id);
//
//    ResponseEntity updateProductById(@RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult,
//                                     @PathVariable("id") UUID id);
//
//    ResponseEntity getAllProducts();

}