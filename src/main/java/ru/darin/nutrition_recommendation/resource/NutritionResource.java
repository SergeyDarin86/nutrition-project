package ru.darin.nutrition_recommendation.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.darin.nutrition_recommendation.dto.*;

import java.util.List;
import java.util.UUID;

public interface NutritionResource {

    @Operation(
            summary = "Отображение общих данных.",
            description = "Возвращает представление общей страницы для пользователей." +
                    "\nНеобходимо выбрать интересующий нас раздел."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Страница успешно получена",
                    content = @Content(mediaType = "text/html")
            ),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    String getCommonPage();

    @Operation(
            summary = "Форма для ввода данных о новом пользователе.",
            description = "Данный метод предназначен для ввода информации о новом пользователе." +
                    "\nНеобходимо заполнить пустые поля."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Страница для создания нового человека успешно получена",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    String newPerson(@ModelAttribute PersonDTO personDTO);


    @Operation(summary = "Сохранение нового пользователя.",
            description = "Создает новую запись о человеке на основе переданных данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Человек успешно добавлен",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "400",
                    description = "Ошибка валидации данных",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "500",
                    description = "Ошибка сервера")
    })
    String createPerson(@ModelAttribute("personDTO") @Valid PersonDTO personDTO, BindingResult bindingResult);

    String showPerson(
            @PathVariable("id") UUID id, Model model, @ModelAttribute("protocolDTO") ProtocolDTO protocolDTO,
            Model protocolListModel
    );

    String editPerson(Model model, @PathVariable("id") UUID id);

    String updatePerson(
            @ModelAttribute("personDTO") @Valid PersonDTO personDTO, BindingResult bindingResult,
            @PathVariable("id") UUID id
    );

    String addProtocolToPerson(
            Model protocolListModel,
            @ModelAttribute("protocolDTO") ProtocolDTO protocolDTO,
            @PathVariable("id") UUID personId
    );

    String curePerson(
            @ModelAttribute("protocolDTO") ProtocolDTO protocolDTO,
            @PathVariable("id") UUID personId,
            @PathVariable("protocol") String protocol
    );

    String deletePerson(@PathVariable("id") UUID id);

    String people(Model model);

    String newProductType(@ModelAttribute ProductTypeDTO productTypeDTO);

    String createProductType(@ModelAttribute("productTypeDTO") @Valid ProductTypeDTO productTypeDTO, BindingResult bindingResult);

    String showProductType(@PathVariable("id") UUID id, Model model, Model allergensList);

    String editProductType(Model model, @PathVariable("id") UUID id);

    String updateProductType(
            @ModelAttribute("productTypeDTO") @Valid ProductTypeDTO productTypeDTO,
            BindingResult bindingResult,
            @PathVariable("id") UUID id
    );

    String deleteProductType(@PathVariable("id") UUID id);

    String getProductTypeList(Model model);

    String newProduct(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute ProductDTO productDTO,
            Model allergenList

    );

    String createProduct(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute("productDTO") @Valid ProductDTO productDTO, BindingResult bindingResult,
            Model allergenList,
            @RequestParam(value = "allergen", required = false) List<UUID> selectedAllergens
    );

    String showProduct(
            @PathVariable("id") UUID id, Model model,
            @PathVariable("typeId") UUID typeId, Model modelType
    );

    String editProduct(
            @PathVariable("id") UUID id, Model model,
            @PathVariable("typeId") UUID typeId, Model modelType,
            Model allergenList
    );

    String updateProduct(
            @PathVariable("id") UUID id,
            @ModelAttribute("productDTO") @Valid ProductDTO productDTO, BindingResult bindingResult,
            @PathVariable("typeId") UUID typeId, Model modelType,
            Model allergenList,
            @RequestParam(value = "allergen", required = false) List<UUID> selectedAllergens
    );

    String deleteProduct(
            @PathVariable("id") UUID id, Model model,
            @PathVariable("typeId") UUID typeId, Model modelType
    );

    String newProtocol(@ModelAttribute ProtocolDTO protocolDTO);

    String createProtocol(@ModelAttribute("protocolDTO") @Valid ProtocolDTO protocolDTO, BindingResult bindingResult);

    String showProtocol(@PathVariable("id") UUID id, Model model);

    String editProtocol(Model model, @PathVariable("id") UUID id);

    String updateProtocol(
            @ModelAttribute("protocolDTO") @Valid ProtocolDTO protocolDTO, BindingResult bindingResult,
            @PathVariable("id") UUID id
    );

    String deleteProtocol(@PathVariable("id") UUID id);

    String protocolList(Model model);

    String newMix(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute MixDTO mixDTO,
            @ModelAttribute ProductDTO productDTO,
            Model productsModel,
            Model response
    );

    String createMix(
            @PathVariable("id") UUID id, Model model,
            @ModelAttribute("mixDTO") @Valid MixDTO mixDTO, BindingResult bindingResult,
            @ModelAttribute("productDTO") ProductDTO productDTO,
            RedirectAttributes redirectAttributes
    );

    String deleteFromMix(
            @PathVariable("id") UUID id,
            Model model,
            @PathVariable("product") String product,
            Model productModel
    );

    String showMixOfProductsForOneOreTwoProtocols(
            @PathVariable("id") UUID id,
            Model modelProtocol,
            @ModelAttribute("resolutionDTO") @Valid ResolutionDTO resolutionDTO,
            Model model,
            Model protocolList,
            @ModelAttribute("protocolTwo") ProtocolDTO protocolTwo,
            Model allergensList
    );

    String newAllergenType(@ModelAttribute AllergenTypeDTO allergenTypeDTO);

    String createAllergenType(@ModelAttribute("allergenTypeDTO") @Valid AllergenTypeDTO allergenTypeDTO, BindingResult bindingResult);

    String showAllergenType(
            @PathVariable("id") UUID id,
            Model model,
            Model colorModel
    );

    String editAllergenType(Model model, @PathVariable("id") UUID id);

    String updateAllergenType(
            @ModelAttribute("allergenTypeDTO") @Valid AllergenTypeDTO allergenTypeDTO, BindingResult bindingResult,
            @PathVariable("id") UUID id
    );

    String deleteAllergenType(@PathVariable("id") UUID id);

    String allAllergenTypes(Model model);

}