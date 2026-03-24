package ru.darin.nutrition_recommendation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.mapper.PersonMapper;
import ru.darin.nutrition_recommendation.model.Person;
import ru.darin.nutrition_recommendation.repository.PersonRepository;
import ru.darin.nutrition_recommendation.util.exception.NutritionExceptionNotFound;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NutritionServiceForThymeleafTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private NutritionServiceForThymeleaf personService;

    private UUID personUuid;
    private Person person;
    private PersonDTO personDTOActual;

    @BeforeEach
    void setUp() {
        personUuid = UUID.randomUUID();
        person = new Person();
        person.setPersonId(personUuid);

        personDTOActual = new PersonDTO();
        personDTOActual.setPersonId(personUuid);
        personDTOActual.setFullName("Иванов Иван Иванович");
    }

    @Test
    void testGetPersonByIdSuccess() {

        when(personRepository.findById(personUuid)).thenReturn(Optional.of(person));
        when(personMapper.toPersonDto(person)).thenReturn(personDTOActual);

        personService.getPersonById(personUuid);

        PersonDTO personDTOExpected = new PersonDTO();
        personDTOExpected.setPersonId(personUuid);
        personDTOExpected.setFullName("Иванов Иван Иванович");

        assertEquals(personDTOExpected, personDTOActual);

        verify(personRepository, times(1)).findById(personUuid);
    }

    @Test
    void testGetPersonByIdWhenPersonNotFound() {
        UUID illegalUuid = UUID.randomUUID();

        when(personRepository.findById(illegalUuid)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NutritionExceptionNotFound.class,()->personService.getPersonById(illegalUuid));
        assertEquals("Пользователь не найден", exception.getMessage());

        verify(personRepository, times(1)).findById(illegalUuid);
    }
}