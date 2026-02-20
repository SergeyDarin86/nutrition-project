package ru.darin.nutrition_recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.darin.nutrition_recommendation.controller.DefaultController;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.dto.ProtocolDTO;
import ru.darin.nutrition_recommendation.service.NutritionServiceForThymeleaf;

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

    @BeforeEach
    void setUp() {
        personDTO = new PersonDTO();
        personDTO.setFullName("Иванов Иван Иванович");
        protocolDTO = new ProtocolDTO();
        protocolDTO.setProtocolTitle("ЭРД");
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
    public void testUpdatePerson_WithReturnToForm() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/nutrition/people/{id}", id)
                        .param("fullName", "Иванов Иван Иванович"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/people/" + id));

        verify(nutritionService, times(1)).updatePersonById(eq(id), any(PersonDTO.class));
    }

    @Test
    public void testUpdatePerson_WithRedirect() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/nutrition/people/{id}", id)
                        .param("fullName", "иванов Иван Иванович"))
                .andExpect(status().isOk())
                .andExpect(view().name("people/editPerson"));

        verify(nutritionService, times(0)).updatePersonById(eq(id), any(PersonDTO.class));
    }

    @Test
    public void testAddProtocolToPerson() throws Exception{
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/nutrition/people/{id}/addProtocolToPerson", id)
                        .param("fullName", "Иванов Иван Иванович"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nutrition/people/" + id));

        verify(nutritionService, times(1)).addProtocolToPerson(eq(id), any(ProtocolDTO.class));
    }

}