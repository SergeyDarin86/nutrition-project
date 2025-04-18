package ru.darin.nutrition_recommendation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.darin.nutrition_recommendation.dto.*;
import ru.darin.nutrition_recommendation.service.NutritionServiceForThymeleaf;

import java.util.UUID;

@Controller
@RequestMapping("/nutrition")
@RequiredArgsConstructor
public class DefaultController {

    private final NutritionServiceForThymeleaf nutritionService;

    @GetMapping("/commonPage")
    public String getCommonPage(){
        return "people/commonPage";
    }

    @GetMapping("/newPerson")
    public String newPerson(@ModelAttribute PersonDTO personDTO) {
        return "people/newPerson";
    }

    @PostMapping("/addPerson")
    public String createPerson(@ModelAttribute("personDTO") @Valid PersonDTO personDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/newPerson";
        nutritionService.addPerson(personDTO);
        return "redirect:/nutrition/people";
    }

    @GetMapping("/newProductType")
    public String newProductType(@ModelAttribute ProductTypeDTO productTypeDTO) {
        return "products/newProductType";
    }

    @PostMapping("/addProductType")
    public String createProductType(@ModelAttribute("productTypeDTO") @Valid ProductTypeDTO productTypeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "products/newProductType";
        nutritionService.addProductType(productTypeDTO);
        return "redirect:/nutrition/productTypeList";
    }

    @GetMapping("/newIllness")
    public String newIllness(@ModelAttribute IllnessDTO illnessDTO) {
        return "illnesses/newIllness";
    }

    @PostMapping("/addIllness")
    public String createIllness(@ModelAttribute("illnessDTO") @Valid IllnessDTO illnessDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "illnesses/newIllness";
        nutritionService.addIllness(illnessDTO);
        return "redirect:/nutrition/allIllnesses";
    }

    @GetMapping("/productType/{id}/newProduct")
    public String newProduct(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute ProductDTO productDTO
    ) {
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(id));
        return "products/newProduct";
    }

    @PostMapping("/productType/{id}/addProduct")
    public String createProduct(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute("productDTO") @Valid ProductDTO productDTO, BindingResult bindingResult) {
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(id));
        if (bindingResult.hasErrors())
            return "products/newProduct";

        nutritionService.addProduct(productDTO, id);
        return "redirect:/nutrition/productType/{id}";
    }
// ----> рабочие методы <----
    @GetMapping("/allIllnesses/{id}/newMix")
    public String newMix(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute MixDTO mixDTO,
            @ModelAttribute ProductDTO productDTO,
            Model productsModel
    ) {
        productsModel.addAttribute("productsList", nutritionService.getAllProducts());
        model.addAttribute("illnessDTO", nutritionService.getIllnessById(id));
        return "mix/newMix";
    }

    @PostMapping("/allIllnesses/{id}/addMix")
    public String createMix(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute("mixDTO") @Valid MixDTO mixDTO, BindingResult bindingResult,
            @ModelAttribute("productDTO") ProductDTO productDTO
    ) {
        model.addAttribute("illnessDTO", nutritionService.getIllnessById(id));
        if (bindingResult.hasErrors())
            return "mix/newMix";
        nutritionService.addMixOfProductsAndIllnesses(mixDTO, id, productDTO.getProductId());
        return "redirect:/nutrition/allIllnesses/{id}/newMix";
    }

    // ------ рабочие методы


    @GetMapping("/people/{id}")
    public String showPerson(
            @PathVariable("id") UUID id,
            Model model,
            @ModelAttribute ("illnessDTO") IllnessDTO illnessDTO,
            Model illnessesListModel
    ) {
        illnessesListModel.addAttribute("illnessList", nutritionService.getAllIllnesses());
        model.addAttribute("personDTO", nutritionService.getPersonById(id));
        return "people/showPerson";
    }

    @GetMapping("/people/{id}/edit")
    public String editPerson(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("personDTO", nutritionService.getPersonById(id));
        return "people/editPerson";
    }

    @PatchMapping("/people/{id}")
    public String updatePerson(
            @ModelAttribute("personDTO") @Valid PersonDTO personDTO, BindingResult bindingResult,
            @PathVariable("id") UUID id
    ) {
        if (bindingResult.hasErrors())
            return "people/editPerson";

        nutritionService.updatePersonById(id,personDTO);
        return "redirect:/nutrition/people/{id}";
    }

    @PatchMapping("/people/{id}/addIllnessToPerson")
    public String addIllnessToPerson(
            Model illnessesListModel,
            @ModelAttribute ("illnessDTO") IllnessDTO illnessDTO,
            @PathVariable("id") UUID personId
    ) {
        illnessesListModel.addAttribute("illnessList", nutritionService.getAllIllnesses());
        nutritionService.addIllnessToPerson(personId, illnessDTO);
        return "redirect:/nutrition/people/{id}";
    }

    @PatchMapping("/people/{id}/curePerson")
    public String curePerson(
            @ModelAttribute ("illnessDTO") IllnessDTO illnessDTO,
            @PathVariable("id") UUID personId
    ) {
        nutritionService.curePerson(personId);
        return "redirect:/nutrition/people/{id}";
    }

    @DeleteMapping("/people/{id}")
    public String deletePerson(@PathVariable("id") UUID id) {
        nutritionService.deletePersonById(id);
        return "redirect:/nutrition/people";
    }

    @DeleteMapping("/allIllnesses/{id}")
    public String deleteIllness(@PathVariable("id") UUID id) {
        nutritionService.deleteIllnessById(id);
        return "redirect:/nutrition/allIllnesses";
    }

    @DeleteMapping("/productType/{typeId}/product/{id}")
    public String deleteProduct(
            @PathVariable("id") UUID id, Model model,
            @PathVariable("typeId") UUID typeId, Model modelType
    ) {
        nutritionService.deleteProductById(id);
        return "redirect:/nutrition/productType/{typeId}";
    }

    @DeleteMapping("/productType/{id}")
    public String deleteProductType(@PathVariable("id") UUID id) {
        nutritionService.deleteProductTypeById(id);
        return "redirect:/nutrition/productTypeList";
    }

    @GetMapping("/allIllnesses/{id}")
    public String showIllness(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("illnessDTO", nutritionService.getIllnessById(id));
        return "illnesses/showIllness";
    }

    @GetMapping("/allIllnesses/{id}/edit")
    public String editIllness(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("illnessDTO", nutritionService.getIllnessById(id));
        return "illnesses/editIllness";
    }

    @PatchMapping("/allIllnesses/{id}")
    public String updateIllness(
            @ModelAttribute("illnessDTO") @Valid IllnessDTO illnessDTO, BindingResult bindingResult,
            @PathVariable("id") UUID id
    ) {
        if (bindingResult.hasErrors())
            return "illnesses/editIllness";

        nutritionService.updateIllnessById(id,illnessDTO);
        return "redirect:/nutrition/allIllnesses/{id}";
    }

    @GetMapping("/productType/{id}/edit")
    public String editProductType(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(id));
        return "products/editProductType";
    }

    @PatchMapping("/productType/{id}")
    public String updateProduct(
            @ModelAttribute("productTypeDTO") @Valid ProductTypeDTO productTypeDTO, BindingResult bindingResult,
            @PathVariable("id") UUID id
    ) {
        if (bindingResult.hasErrors())
            return "products/editProductType";

        nutritionService.updateProductTypeById(id,productTypeDTO);
        return "redirect:/nutrition/productType/{id}";
    }

    @GetMapping("/productType/{typeId}/product/{id}/edit")
    public String editProduct(
            @PathVariable("id") UUID id, Model model,
            @PathVariable("typeId") UUID typeId, Model modelType
    ) {
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(typeId));
        modelType.addAttribute("productDTO", nutritionService.getProductById(id));
        return "products/editProduct";
    }

    @PatchMapping("/productType/{typeId}/product/{id}")
    public String updateProduct(
            @PathVariable("id") UUID id,
            @ModelAttribute("productDTO") @Valid ProductDTO productDTO, BindingResult bindingResult,
            @PathVariable("typeId") UUID typeId, Model modelType
    ) {
        modelType.addAttribute("productTypeDTO", nutritionService.getProductTypeById(typeId));
        if (bindingResult.hasErrors())
            return "products/editProduct";

        nutritionService.updateProductById(id, productDTO);
        return "redirect:/nutrition/productType/{typeId}/product/{id}";
    }

    @GetMapping("/people")
    public String people(Model model) {
        model.addAttribute("people", nutritionService.getAllPeople());
        return "people/people";
    }

    @GetMapping("/allIllnesses")
    public String illnessList(Model model) {
        model.addAttribute("illnessList", nutritionService.getAllIllnesses());
        return "illnesses/illnessList";
    }

    @GetMapping("/productTypeList")
    public String getProductTypeList(Model model) {
        model.addAttribute("productTypeList", nutritionService.getAllProductTypes());
        return "products/productTypeList";
    }

    @GetMapping("/productType/{id}")
    public String showProductType(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(id));
        return "products/showProductType";
    }

    @GetMapping("/productType/{typeId}/product/{id}")
    public String showProduct(
            @PathVariable("id") UUID id, Model model,
            @PathVariable("typeId") UUID typeId, Model modelType
    ) {
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(typeId));
        modelType.addAttribute("productDTO", nutritionService.getProductById(id));
        return "products/showProduct";
    }

}