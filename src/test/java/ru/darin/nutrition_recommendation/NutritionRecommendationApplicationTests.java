package ru.darin.nutrition_recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.darin.nutrition_recommendation.controller.DefaultController;
import ru.darin.nutrition_recommendation.dto.*;
import ru.darin.nutrition_recommendation.service.NutritionServiceForThymeleaf;
import ru.darin.nutrition_recommendation.util.RecommendationResponseWithDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NutritionRecommendationApplicationTests {

    @Autowired
    private DefaultController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NutritionServiceForThymeleaf nutritionService;

    @Autowired
    private ObjectMapper mapper;

    PersonDTO personDTO;

    ProtocolDTO protocolDTO;

    UUID protocolId;

    UUID productTypeId = UUID.randomUUID();

    ProductTypeDTO productTypeDTO;

    UUID productId;

    ProductDTO productDTO;

    RecommendationResponseWithDTO response;

    AllergenTypeDTO allergenTypeDTO;

    UUID allergenTypeId;

    String titleColor;

    @BeforeEach
    void setUp() {
        personDTO = new PersonDTO();
        personDTO.setFullName("Иванов Иван Иванович");

        protocolId = UUID.randomUUID();
        protocolDTO = new ProtocolDTO();
        protocolDTO.setProtocolTitle("ЭРД");

        productTypeDTO = new ProductTypeDTO();
        productTypeDTO.setProductType("Бобовые");

        productDTO = new ProductDTO();
        productDTO.setProduct("Горох");

        productId = UUID.randomUUID();

        response = new RecommendationResponseWithDTO();
        response.setProtocol("ЭРД");
        response.setResolution("РАЗРЕШЕНО");

        allergenTypeId = UUID.randomUUID();
        allergenTypeDTO = new AllergenTypeDTO(allergenTypeId, "Цитрусовые");
        titleColor = "красный";
        allergenTypeDTO.setTitleColor(titleColor);

    }

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void testGetCommonPage() throws Exception {
        mockMvc.perform(get("/nutrition/commonPage"))
                .andExpect(view().name("people/commonPage"));
    }

    @Test
    public void testNewPerson() throws Exception {
        mockMvc.perform(get("/nutrition/newPerson"))
                .andExpect(view().name("people/newPerson"));
    }

    @Test
    public void testCreatePerson_WithRedirect() throws Exception {
        mockMvc.perform(post("/nutrition/addPerson")
                        .param("fullName", "Иванов Иван Иванович"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/people"));

        verify(nutritionService, times(1)).addPerson(any(PersonDTO.class));
    }

    @Test
    public void testCreatePerson_WithReturnToForm() throws Exception {
        mockMvc.perform(post("/nutrition/addPerson")
                        .param("fullName", "иванов Иван Иванович"))
                .andExpect(status().isOk())
                .andExpect(view().name("people/newPerson"));

        verify(nutritionService, times(0)).addPerson(any(PersonDTO.class));
    }

    @Test
    public void testShowPerson() throws Exception {
        UUID id = UUID.randomUUID();
        personDTO.setProtocols(Arrays.asList(protocolDTO));

        when(nutritionService.getAllProtocols()).thenReturn(Arrays.asList(protocolDTO));
        when(nutritionService.getPersonById(id)).thenReturn(personDTO);

        mockMvc.perform(get("/nutrition/people/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("people/showPerson"))
                .andExpect(model().attributeExists("personDTO"))
                .andExpect(model().attributeExists("protocolList"));
    }

    @Test
    public void testEditPerson() throws Exception {
        UUID id = UUID.randomUUID();

        when(nutritionService.getPersonById(id)).thenReturn(personDTO);

        mockMvc.perform(get("/nutrition/people/{id}/edit", id))
                .andExpect(status().isOk())
                .andExpect(view().name("people/editPerson"))
                .andExpect(model().attributeExists("personDTO"));
    }

    @Test
    public void testUpdatePerson_WithRedirect() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/nutrition/people/{id}", id)
                        .param("fullName", "Иванов Иван Иванович"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/people/" + id));

        verify(nutritionService, times(1)).updatePersonById(eq(id), any(PersonDTO.class));
    }

    @Test
    public void testUpdatePerson_WithReturnToForm() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/nutrition/people/{id}", id)
                        .param("fullName", "иванов Иван Иванович"))
                .andExpect(status().isOk())
                .andExpect(view().name("people/editPerson"));

        verify(nutritionService, times(0)).updatePersonById(eq(id), any(PersonDTO.class));
    }

    @Test
    public void testAddProtocolToPerson() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/nutrition/people/{id}/addProtocolToPerson", id)
                        .param("fullName", "Иванов Иван Иванович"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/people/" + id));

        verify(nutritionService, times(1)).addProtocolToPerson(eq(id), any(ProtocolDTO.class));
    }

    @Test
    public void testCurePerson() throws Exception {
        UUID id = UUID.randomUUID();
        String protocol = "ЭРД";

        mockMvc.perform(patch("/nutrition/people/{id}/curePerson/{protocol}", id, protocol))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/people/" + id));

        verify(nutritionService, times(1)).curePerson(eq(id), eq(protocol));
    }

    @Test
    public void testDeletePerson() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/nutrition/people/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/people"));

        verify(nutritionService, times(1)).deletePersonById(eq(id));
    }

    @Test
    public void testShowAllPeople() throws Exception {
        mockMvc.perform(get("/nutrition/people"))
                .andExpect(status().isOk())
                .andExpect(view().name("people/people"));
    }

    @Test
    public void testNewProductType() throws Exception {
        mockMvc.perform(get("/nutrition/newProductType"))
                .andExpect(view().name("products/newProductType"));
    }

    @Test
    public void testCreateProductType_WithRedirect() throws Exception {
        mockMvc.perform(post("/nutrition/addProductType")
                        .param("productType", "Бобовые"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/productTypeList"));

        verify(nutritionService, times(1)).addProductType(any(ProductTypeDTO.class));
    }

    @Test
    public void testCreateProductType_WithReturnToForm() throws Exception {
        mockMvc.perform(post("/nutrition/addProductType")
                        .param("productType", "бобовые"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/newProductType"));

        verify(nutritionService, times(0)).addProductType(any(ProductTypeDTO.class));
    }

    @Test
    public void testShowProductType() throws Exception {
        when(nutritionService.getProductTypeById(productTypeId)).thenReturn(productTypeDTO);

        mockMvc.perform(get("/nutrition/productType/{id}", productTypeId))
                .andExpect(status().isOk())
                .andExpect(view().name("products/showProductType"))
                .andExpect(model().attributeExists("productTypeDTO"));
    }

    @Test
    public void testEditProductType() throws Exception {
        when(nutritionService.getProductTypeById(productTypeId)).thenReturn(productTypeDTO);

        mockMvc.perform(get("/nutrition/productType/{id}/edit", productTypeId))
                .andExpect(status().isOk())
                .andExpect(view().name("products/editProductType"))
                .andExpect(model().attributeExists("productTypeDTO"));
    }

    @Test
    public void testUpdateProductType_WithRedirect() throws Exception {
        mockMvc.perform(patch("/nutrition/productType/{id}", productTypeId)
                        .param("productType", "Бобовые"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/productType/" + productTypeId));

        verify(nutritionService, times(1)).updateProductTypeById(eq(productTypeId), any(ProductTypeDTO.class));
    }


    @Test
    public void testUpdateProductType_WithReturnToForm() throws Exception {
        mockMvc.perform(patch("/nutrition/productType/{id}", productTypeId)
                        .param("productType", "бобовые"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/editProductType"));

        verify(nutritionService, times(0)).updateProductTypeById(eq(productTypeId), any(ProductTypeDTO.class));
    }

    @Test
    public void testDeleteProductType() throws Exception {
        mockMvc.perform(delete("/nutrition/productType/{id}", productTypeId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/productTypeList"));

        verify(nutritionService, times(1)).deleteProductTypeById(eq(productTypeId));
    }

    @Test
    public void testShowProductTypeList() throws Exception {
        mockMvc.perform(get("/nutrition/productTypeList"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/productTypeList"));
    }

    @Test
    public void testNewProduct() throws Exception {
        when(nutritionService.getProductTypeById(productTypeId)).thenReturn(productTypeDTO);

        mockMvc.perform(get("/nutrition/productType/{id}/newProduct", productTypeId))
                .andExpect(view().name("products/newProduct"));
    }

    @Test
    public void testCreateProduct_WithRedirect() throws Exception {
        when(nutritionService.getProductTypeById(productTypeId)).thenReturn(productTypeDTO);

        mockMvc.perform(post("/nutrition/productType/{id}/addProduct", productTypeId)
                        .param("product", "Горох"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/productType/" + productTypeId));

        verify(nutritionService, times(1)).addProduct(productDTO, productTypeId, null);
    }

    @Test
    public void testCreateProduct_WithReturnToForm() throws Exception {
        when(nutritionService.getProductTypeById(productTypeId)).thenReturn(productTypeDTO);

        mockMvc.perform(post("/nutrition/productType/{id}/addProduct", productTypeId)
                        .param("product", "горох"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/newProduct"));

        verify(nutritionService, times(0)).addProduct(productDTO, productTypeId, null);
    }

    @Test
    public void testShowProduct() throws Exception {
        when(nutritionService.getProductById(productId)).thenReturn(productDTO);
        when(nutritionService.getProductTypeById(productTypeId)).thenReturn(productTypeDTO);
        productDTO.setAllergenTypes(new ArrayList<>());

        mockMvc.perform(get("/nutrition/productType/{typeId}/product/{id}", productTypeId, productId))
                .andExpect(status().isOk())
                .andExpect(view().name("products/showProduct"))
                .andExpect(model().attributeExists("productDTO"));
    }

    @Test
    public void testEditProduct() throws Exception {
        when(nutritionService.getProductById(productId)).thenReturn(productDTO);
        when(nutritionService.getProductTypeById(productTypeId)).thenReturn(productTypeDTO);
        when(nutritionService.getAllergenTypes()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/nutrition/productType/{typeId}/product/{id}/edit", productTypeId, productId))
                .andExpect(status().isOk())
                .andExpect(view().name("products/editProduct"))
                .andExpect(model().attributeExists("productDTO"));
    }

    @Test
    public void testUpdateProduct_WithRedirect() throws Exception {
        mockMvc.perform(patch("/nutrition/productType/{typeId}/product/{id}", productTypeId, productId)
                        .param("product", "Горох"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/productType/" + productTypeId + "/product/" + productId));

        verify(nutritionService, times(1)).updateProductById(productId, productDTO, null);
    }

    @Test
    public void testUpdateProduct_WithReturnToForm() throws Exception {
        when(nutritionService.getProductTypeById(productTypeId)).thenReturn(productTypeDTO);

        mockMvc.perform(patch("/nutrition/productType/{typeId}/product/{id}", productTypeId, productId)
                        .param("product", "горох"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/editProduct"));

        verify(nutritionService, times(0)).updateProductById(productId, productDTO, null);
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/nutrition/productType/{typeId}/product/{id}", productTypeId, productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/productType/" + productTypeId));

        verify(nutritionService, times(1)).deleteProductById(eq(productId));
    }

    @Test
    public void testNewProtocol() throws Exception {
        mockMvc.perform(get("/nutrition/newProtocol"))
                .andExpect(view().name("protocols/newProtocol"));
    }

    @Test
    public void testCreateProtocol_WithRedirect() throws Exception {
        mockMvc.perform(post("/nutrition/addProtocol")
                        .param("protocolTitle", "ЭРД"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/allProtocols"));

        verify(nutritionService, times(1)).addProtocol(any(ProtocolDTO.class));
    }

    @Test
    public void testCreateProtocol_WithReturnToForm() throws Exception {
        mockMvc.perform(post("/nutrition/addProtocol")
                        .param("protocolTitle", "эрд"))
                .andExpect(status().isOk())
                .andExpect(view().name("protocols/newProtocol"));

        verify(nutritionService, times(0)).addProtocol(any(ProtocolDTO.class));
    }

    @Test
    public void testShowProtocol() throws Exception {
        when(nutritionService.getProtocolById(protocolId)).thenReturn(protocolDTO);

        mockMvc.perform(get("/nutrition/allProtocols/{id}", protocolId))
                .andExpect(status().isOk())
                .andExpect(view().name("protocols/showProtocol"))
                .andExpect(model().attributeExists("protocolDTO"));
    }

    @Test
    public void testEditProtocol() throws Exception {
        when(nutritionService.getProtocolById(protocolId)).thenReturn(protocolDTO);

        mockMvc.perform(get("/nutrition/allProtocols/{id}/edit", protocolId))
                .andExpect(status().isOk())
                .andExpect(view().name("protocols/editProtocol"))
                .andExpect(model().attributeExists("protocolDTO"));
    }

    @Test
    public void testUpdateProtocol_WithRedirect() throws Exception {
        mockMvc.perform(patch("/nutrition/allProtocols/{id}", protocolId)
                        .param("protocolTitle", "ЭРД"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/allProtocols/" + protocolId));

        verify(nutritionService, times(1)).updateProtocolById(eq(protocolId), any(ProtocolDTO.class));
    }

    @Test
    public void testUpdateProtocol_WithReturnToForm() throws Exception {
        mockMvc.perform(patch("/nutrition/allProtocols/{id}", protocolId)
                        .param("protocolTitle", "эРД"))
                .andExpect(status().isOk())
                .andExpect(view().name("protocols/editProtocol"));

        verify(nutritionService, times(0)).updateProtocolById(eq(protocolId), any(ProtocolDTO.class));
    }

    @Test
    public void testDeleteProtocol() throws Exception {
        mockMvc.perform(delete("/nutrition/allProtocols/{id}", protocolId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/allProtocols"));

        verify(nutritionService, times(1)).deleteProtocolById(eq(protocolId));
    }

    @Test
    public void testShowProtocolList() throws Exception {
        mockMvc.perform(get("/nutrition/allProtocols"))
                .andExpect(status().isOk())
                .andExpect(view().name("protocols/protocolList"));
    }

    @Test
    public void testNewMix() throws Exception {
        when(nutritionService.getProtocolById(protocolId)).thenReturn(protocolDTO);

        mockMvc.perform(get("/nutrition/allProtocols/{id}/newMix", protocolId))
                .andExpect(view().name("mix/newMix"));
    }

    @Test
    public void testCreateMix_WithRedirect() throws Exception {
        when(nutritionService.getProtocolById(protocolId)).thenReturn(protocolDTO);

        mockMvc.perform(post("/nutrition/allProtocols/{id}/addMix", protocolId)
                        .param("protocolTitle", "ЭРД"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/allProtocols/" + protocolId + "/newMix"));

        verify(nutritionService, times(1)).addMixOfProductsAndProtocols(any(MixDTO.class), eq(protocolId), eq(productDTO.getProductId()));
    }

    @Test
    public void testDeleteFromMix() throws Exception {

        String product = "Горох";
        when(nutritionService.getProductDTOByProductName(product)).thenReturn(productDTO);
        productDTO.setProductId(productId);

        mockMvc.perform(delete("/nutrition/allProtocols/{id}/deleteFromMix/{product}", protocolId, product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/allProtocols/" + protocolId + "/newMix"));

        verify(nutritionService, times(1)).deleteMixOfProductAndIllnessByProductIdWithIllnessId(productId, protocolId);
    }

    @Test
    public void testNewAllergenType() throws Exception {
        mockMvc.perform(get("/nutrition/newAllergenType"))
                .andExpect(view().name("allergens/newAllergenType"));
    }

    @Test
    public void testCreateAllergenType_WithRedirect() throws Exception {
        mockMvc.perform(post("/nutrition/addAllergenType")
                        .param("allergenTitle", "Цитрусовые"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/allAllergenTypes"));

        verify(nutritionService, times(1)).addAllergenType(any(AllergenTypeDTO.class));
    }

    @Test
    public void testCreateAllergenType_WithReturnToForm() throws Exception {
        mockMvc.perform(post("/nutrition/addAllergenType")
                        .param("allergenTitle", "цитрусовые"))
                .andExpect(status().isOk())
                .andExpect(view().name("allergens/newAllergenType"));

        verify(nutritionService, times(0)).addAllergenType(any(AllergenTypeDTO.class));
    }

    @Test
    public void testShowAllergenType() throws Exception {
        when(nutritionService.getAllergenTypeById(allergenTypeId)).thenReturn(allergenTypeDTO);

        mockMvc.perform(get("/nutrition/allAllergenTypes/{id}", allergenTypeId))
                .andExpect(status().isOk())
                .andExpect(view().name("allergens/showAllergenType"))
                .andExpect(model().attributeExists("allergenTypeDTO"))
                .andExpect(model().attributeExists("titleColor"));
    }

    @Test
    public void testEditAllergenType() throws Exception {
        when(nutritionService.getAllergenTypeById(allergenTypeId)).thenReturn(allergenTypeDTO);

        mockMvc.perform(get("/nutrition/allAllergenTypes/{id}/edit", allergenTypeId))
                .andExpect(status().isOk())
                .andExpect(view().name("allergens/editAllergenType"))
                .andExpect(model().attributeExists("allergenTypeDTO"));
    }

    @Test
    public void testUpdateAllergenType_WithRedirect() throws Exception {
        mockMvc.perform(patch("/nutrition/allAllergenTypes/{id}", allergenTypeId)
                        .param("allergenTitle", "Цитрусовые"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/allAllergenTypes/" + allergenTypeId));

        verify(nutritionService, times(1)).updateAllergenTypeById(eq(allergenTypeId), any(AllergenTypeDTO.class));
    }

    @Test
    public void testUpdateAllergenType_WithReturnToForm() throws Exception {
        mockMvc.perform(patch("/nutrition/allAllergenTypes/{id}", allergenTypeId)
                        .param("allergenTitle", "цитрусовые"))
                .andExpect(status().isOk())
                .andExpect(view().name("allergens/editAllergenType"));

        verify(nutritionService, times(0)).updateAllergenTypeById(eq(allergenTypeId), any(AllergenTypeDTO.class));
    }

    @Test
    public void testDeleteAllergenType() throws Exception {
        mockMvc.perform(delete("/nutrition/allAllergenTypes/{id}", allergenTypeId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/allAllergenTypes"));

        verify(nutritionService, times(1)).deleteAllergenTypeById(allergenTypeId);
    }

    @Test
    public void testAllAllergenTypes() throws Exception {
        mockMvc.perform(get("/nutrition/allAllergenTypes"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("allergens/allAllergenTypes"));

        verify(nutritionService, times(1)).getAllergenTypes();
    }
}