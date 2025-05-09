package ru.darin.nutrition_recommendation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.darin.nutrition_recommendation.dto.*;
import ru.darin.nutrition_recommendation.resource.NutritionResource;
import ru.darin.nutrition_recommendation.service.NutritionServiceForThymeleaf;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/nutrition")
@RequiredArgsConstructor
public class DefaultController implements NutritionResource {

    private final NutritionServiceForThymeleaf nutritionService;

    @GetMapping("/commonPage")
    public String getCommonPage() {
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

    @GetMapping("/people/{id}")
    public String showPerson(
            @PathVariable("id") UUID id,
            Model model,
            @ModelAttribute("protocolDTO") ProtocolDTO protocolDTO,
            Model protocolListModel
    ) {
        protocolListModel.addAttribute("protocolList", nutritionService.getAllProtocols());
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

        nutritionService.updatePersonById(id, personDTO);
        return "redirect:/nutrition/people/{id}";
    }

    @PatchMapping("/people/{id}/addProtocolToPerson")
    public String addProtocolToPerson(
            Model protocolListModel,
            @ModelAttribute("protocolDTO") ProtocolDTO protocolDTO,
            @PathVariable("id") UUID personId
    ) {
        protocolListModel.addAttribute("protocolList", nutritionService.getAllProtocols());
        nutritionService.addProtocolToPerson(personId, protocolDTO);
        return "redirect:/nutrition/people/{id}";
    }

    @PatchMapping("/people/{id}/curePerson/{protocol}")
    public String curePerson(
            @ModelAttribute("protocolDTO") ProtocolDTO protocolDTO,
            @PathVariable("id") UUID personId,
            @PathVariable("protocol") String protocol
    ) {
        nutritionService.curePerson(personId, protocol);
        return "redirect:/nutrition/people/{id}";
    }

    @DeleteMapping("/people/{id}")
    public String deletePerson(@PathVariable("id") UUID id) {
        nutritionService.deletePersonById(id);
        return "redirect:/nutrition/people";
    }

    @GetMapping("/people")
    public String people(Model model) {
        model.addAttribute("people", nutritionService.getAllPeople());
        return "people/people";
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

    @GetMapping("/productType/{id}")
    public String showProductType(
            @PathVariable("id") UUID id, Model model
    ) {
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(id));
        return "products/showProductType";
    }

    @GetMapping("/productType/{id}/edit")
    public String editProductType(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(id));
        return "products/editProductType";
    }

    @PatchMapping("/productType/{id}")
    public String updateProductType(
            @ModelAttribute("productTypeDTO") @Valid ProductTypeDTO productTypeDTO, BindingResult bindingResult,
            @PathVariable("id") UUID id
    ) {
        if (bindingResult.hasErrors())
            return "products/editProductType";

        nutritionService.updateProductTypeById(id, productTypeDTO);
        return "redirect:/nutrition/productType/{id}";
    }

    @DeleteMapping("/productType/{id}")
    public String deleteProductType(@PathVariable("id") UUID id) {
        nutritionService.deleteProductTypeById(id);
        return "redirect:/nutrition/productTypeList";
    }

    @GetMapping("/productTypeList")
    public String getProductTypeList(Model model) {
        model.addAttribute("productTypeList", nutritionService.getAllProductTypes());
        return "products/productTypeList";
    }

    @GetMapping("/productType/{id}/newProduct")
    public String newProduct(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute ProductDTO productDTO,
            Model allergenList

    ) {
        allergenList.addAttribute("allergenList", nutritionService.getAllergenTypes());
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(id));
        return "products/newProduct";
    }

    @PostMapping("/productType/{id}/addProduct")
    public String createProduct(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute("productDTO") @Valid ProductDTO productDTO, BindingResult bindingResult,
            Model allergenList,
            @RequestParam(value = "allergen", required = false) List<UUID> selectedAllergens
    ) {
        allergenList.addAttribute("allergenList", nutritionService.getAllergenTypes());
        model.addAttribute("productTypeDTO", nutritionService.getProductTypeById(id));
        if (bindingResult.hasErrors())
            return "products/newProduct";
        nutritionService.addProduct(productDTO, id, selectedAllergens);
        return "redirect:/nutrition/productType/{id}";
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

    @DeleteMapping("/productType/{typeId}/product/{id}")
    public String deleteProduct(
            @PathVariable("id") UUID id, Model model,
            @PathVariable("typeId") UUID typeId, Model modelType
    ) {
        nutritionService.deleteProductById(id);
        return "redirect:/nutrition/productType/{typeId}";
    }

    @GetMapping("/newProtocol")
    public String newProtocol(@ModelAttribute ProtocolDTO protocolDTO) {
        return "protocols/newProtocol";
    }

    @PostMapping("/addProtocol")
    public String createProtocol(@ModelAttribute("protocolDTO") @Valid ProtocolDTO protocolDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "protocols/newProtocol";
        nutritionService.addProtocol(protocolDTO);
        return "redirect:/nutrition/allProtocols";
    }

    @GetMapping("/allProtocols/{id}")
    public String showIllness(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("protocolDTO", nutritionService.getProtocolById(id));
        return "protocols/showProtocol";
    }

    @GetMapping("/allProtocols/{id}/edit")
    public String editProtocol(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("protocolDTO", nutritionService.getProtocolById(id));
        return "protocols/editProtocol";
    }

    @PatchMapping("/allProtocols/{id}")
    public String updateProtocol(
            @ModelAttribute("protocolDTO") @Valid ProtocolDTO protocolDTO, BindingResult bindingResult,
            @PathVariable("id") UUID id
    ) {
        if (bindingResult.hasErrors())
            return "protocols/editProtocol";

        nutritionService.updateProtocolById(id, protocolDTO);
        return "redirect:/nutrition/allProtocols/{id}";
    }

    @DeleteMapping("/allProtocols/{id}")
    public String deleteProtocol(@PathVariable("id") UUID id) {
        nutritionService.deleteProtocolById(id);
        return "redirect:/nutrition/allProtocols";
    }

    @GetMapping("/allProtocols")
    public String protocolList(Model model) {
        model.addAttribute("protocolList", nutritionService.getAllProtocols());
        return "protocols/protocolList";
    }

    @GetMapping("/allProtocols/{id}/newMix")
    public String newMix(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute MixDTO mixDTO,
            @ModelAttribute ProductDTO productDTO,
            Model productsModel,
            Model response
    ) {
        productsModel.addAttribute("productsList", nutritionService.getAllProducts());
        model.addAttribute("protocolDTO", nutritionService.getProtocolById(id));

        String resolution = (String) response.asMap().get("resolution");
        response.addAttribute("response", nutritionService
                .getProtocolWithProductsGroupedByType(nutritionService.getProtocolById(id).getProtocolTitle(), resolution));
        return "mix/newMix";
    }

    @PostMapping("/allProtocols/{id}/addMix")
    public String createMix(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute("mixDTO") @Valid MixDTO mixDTO, BindingResult bindingResult,
            @ModelAttribute("productDTO") ProductDTO productDTO,
            RedirectAttributes redirectAttributes
    ) {
        model.addAttribute("protocolDTO", nutritionService.getProtocolById(id));
        String resolution = mixDTO.getResolution();
        if (bindingResult.hasErrors())
            return "mix/newMix";
        nutritionService.addMixOfProductsAndProtocols(mixDTO, id, productDTO.getProductId());
        redirectAttributes.addFlashAttribute("resolution", resolution);
        return "redirect:/nutrition/allProtocols/{id}/newMix";
    }

    @DeleteMapping("/allProtocols/{id}/deleteFromMix/{product}")
    public String deleteFromMix(
            @PathVariable("id") UUID id,
            Model model,
            @PathVariable("product") String product,
            Model productModel
    ) {
        model.addAttribute("protocolDTO", nutritionService.getProtocolById(id));
        ProductDTO productDTO = nutritionService.getProductDTOByProductName(product);
        productModel.addAttribute("productDTO", productDTO);
        nutritionService.deleteMixOfProductAndIllnessByProductIdWithIllnessId(productDTO.getProductId(), id);
        return "redirect:/nutrition/allProtocols/{id}/newMix";
    }

    // рабочий эндпоинт
//    @GetMapping("/showMix/{id}")
//    public String showMixOfProductsForOneOreTwoProtocols(
//            @PathVariable("id") UUID id,
//            Model modelProtocol,
//            @ModelAttribute("resolutionDTO") @Valid ResolutionDTO resolutionDTO,
//            Model model,
//            Model protocolList,
//            @ModelAttribute("protocolTwo") ProtocolDTO protocolTwo
//    ) {
//        UUID protocolTwoId = protocolTwo.getProtocolId();
//        String protocolTwoTitle = (protocolTwoId != null) ? nutritionService.getProtocolById(protocolTwoId).getProtocolTitle() : null;
//        protocolList.addAttribute("protocolList", nutritionService.getAllProtocols());
//        modelProtocol.addAttribute("protocolDTO", nutritionService.getProtocolById(id));
//        model.addAttribute("response", nutritionService.getMixOfProductsForOneOrTwoIllnesses(nutritionService.getProtocolById(id).getProtocolTitle(), protocolTwoTitle, resolutionDTO.getResolution()));
//        return "protocols/showProtocolWithProducts";
//    }

    @GetMapping("/showMix/{id}")
    public String showMixOfProductsForOneOreTwoProtocols(
            @PathVariable("id") UUID id,
            Model modelProtocol,
            @ModelAttribute("resolutionDTO") @Valid ResolutionDTO resolutionDTO,
            Model model,
            Model protocolList,
            @ModelAttribute("protocolTwo") ProtocolDTO protocolTwo
    ) {
        UUID protocolTwoId = protocolTwo.getProtocolId();
        String protocolTwoTitle = (protocolTwoId != null) ? nutritionService.getProtocolById(protocolTwoId).getProtocolTitle() : null;
        protocolList.addAttribute("protocolList", nutritionService.getAllProtocols());
        modelProtocol.addAttribute("protocolDTO", nutritionService.getProtocolById(id));
        model.addAttribute("response", nutritionService.getMixOfProductsForOneOrTwoIllnesses(nutritionService.getProtocolById(id).getProtocolTitle(), protocolTwoTitle, resolutionDTO.getResolution()));
        return "protocols/showProtocolWithProducts";
    }

    @GetMapping("/newAllergenType")
    public String newAllergenType(@ModelAttribute AllergenTypeDTO allergenTypeDTO) {
        return "allergens/newAllergenType";
    }

    @PostMapping("/addAllergenType")
    public String createAllergenType(@ModelAttribute("allergenTypeDTO") @Valid AllergenTypeDTO allergenTypeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "allergens/newAllergenType";
        nutritionService.addAllergenType(allergenTypeDTO);
        return "redirect:/nutrition/allAllergenTypes";
    }

    @GetMapping("/allAllergenTypes/{id}")
    public String showAllergenType(
            @PathVariable("id") UUID id,
            Model model,
            Model colorModel
    ) {
        colorModel.addAttribute("titleColor", nutritionService.getAllergenTypeById(id).getTitleColor());
        model.addAttribute("allergenTypeDTO", nutritionService.getAllergenTypeById(id));
        return "allergens/showAllergenType";
    }

    @GetMapping("/allAllergenTypes/{id}/edit")
    public String editAllergenType(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("allergenTypeDTO", nutritionService.getAllergenTypeById(id));
        return "allergens/editAllergenType";
    }

    @PatchMapping("/allAllergenTypes/{id}")
    public String updateAllergenType(
            @ModelAttribute("allergenTypeDTO") @Valid AllergenTypeDTO allergenTypeDTO, BindingResult bindingResult,
            @PathVariable("id") UUID id
    ) {
        if (bindingResult.hasErrors())
            return "allergens/editAllergenType";

        nutritionService.updateAllergenTypeById(id, allergenTypeDTO);
        return "redirect:/nutrition/allAllergenTypes/{id}";
    }

    @DeleteMapping("/allAllergenTypes/{id}")
    public String deleteAllergenType(@PathVariable("id") UUID id) {
        nutritionService.deleteAllergenTypeById(id);
        return "redirect:/nutrition/allAllergenTypes";
    }

    @GetMapping("/allAllergenTypes")
    public String allAllergenTypes(Model model) {
        model.addAttribute("allergens", nutritionService.getAllergenTypes());
        return "allergens/allAllergenTypes";
    }

}