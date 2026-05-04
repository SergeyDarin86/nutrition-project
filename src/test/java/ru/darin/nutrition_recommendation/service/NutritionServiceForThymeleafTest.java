package ru.darin.nutrition_recommendation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.dto.ProtocolDTO;
import ru.darin.nutrition_recommendation.mapper.PersonMapper;
import ru.darin.nutrition_recommendation.mapper.ProtocolMapper;
import ru.darin.nutrition_recommendation.model.Person;
import ru.darin.nutrition_recommendation.model.Protocol;
import ru.darin.nutrition_recommendation.repository.PersonRepository;
import ru.darin.nutrition_recommendation.repository.ProtocolRepository;
import ru.darin.nutrition_recommendation.util.exception.NutritionException;
import ru.darin.nutrition_recommendation.util.exception.NutritionExceptionNotFound;

import java.util.ArrayList;
import java.util.List;
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

    @Mock
    ProtocolRepository protocolRepository;

    @Mock
    ProtocolMapper protocolMapper;

    @InjectMocks
    private NutritionServiceForThymeleaf personService;

    private UUID personUuid;

    private Person person;

    private PersonDTO personDTOActual;

    private Protocol protocol;

    private ProtocolDTO protocolDTOActual;

    private UUID protocolUuid;

    private List<PersonDTO> personDTOListActual;

    @BeforeEach
    void setUp() {
        personUuid = UUID.randomUUID();
        person = new Person();
        person.setPersonId(personUuid);

        personDTOActual = new PersonDTO();
        personDTOActual.setPersonId(personUuid);
        personDTOActual.setFullName("Иванов Иван Иванович");

        personDTOListActual = new ArrayList<>();
        personDTOListActual.add(personDTOActual);

        protocolUuid = UUID.randomUUID();
        protocol = new Protocol();
        protocol.setProtocol_id(protocolUuid);
        protocol.setProtocolTitle("ЭРД");

        protocolDTOActual = new ProtocolDTO();
        protocolDTOActual.setProtocolId(protocolUuid);
        protocolDTOActual.setProtocolTitle("ЭРД");
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
        Exception exception = assertThrows(NutritionExceptionNotFound.class, () -> personService.getPersonById(illegalUuid));
        assertEquals("Пользователь не найден", exception.getMessage());

        verify(personRepository, times(1)).findById(illegalUuid);
    }

    @Test
    void testGetAllPeople() {
        List<PersonDTO> personDTOListExpected = new ArrayList<>();
        PersonDTO personDTO = new PersonDTO();
        personDTO.setPersonId(personUuid);
        personDTO.setFullName("Иванов Иван Иванович");
        personDTOListExpected.add(personDTO);

        personService.getAllPeople();

        assertEquals(personDTOListExpected, personDTOListActual);
        verify(personRepository, times(1)).findAll(Sort.by("fullName"));
    }

    @Test
    void testAddPerson() {
        when(personMapper.toPerson(personDTOActual)).thenReturn(person);
        personService.addPerson(personDTOActual);
        verify(personRepository, times(1)).save(Mockito.any(Person.class));
    }

    @Test
    void testUpdatePersonByIdSuccess() {
        PersonDTO updatedPersonDTO = new PersonDTO();
        updatedPersonDTO.setPersonId(personUuid);
        updatedPersonDTO.setFullName("Сидоров Семен Семенович");

        Person personToSave = new Person();
        personToSave.setPersonId(personUuid);
        personToSave.setFullName("Сидоров Семен Семенович");

        when(personRepository.findById(personUuid)).thenReturn(Optional.ofNullable(person));
        when(personMapper.toPerson(updatedPersonDTO)).thenReturn(personToSave);

        personService.updatePersonById(personUuid, updatedPersonDTO);

        verify(personRepository, times(1)).save(Mockito.any(Person.class));
    }

    @Test
    void testUpdatePersonByIdWhenPersonNotFound() {
        UUID illegalUuid = UUID.randomUUID();

        when(personRepository.findById(illegalUuid)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NutritionExceptionNotFound.class, () -> personService.getPersonById(illegalUuid));
        assertEquals("Пользователь не найден", exception.getMessage());

        verify(personRepository, times(0)).save(Mockito.any(Person.class));
    }

    @Test
    void testDeletePersonById() {
        when(personRepository.findById(personUuid)).thenReturn(Optional.of(person));

        personService.deletePersonById(personUuid);

        verify(personRepository, times(1)).delete(person);
    }

    @Test
    void testAddProtocolToPerson() {
        UUID protocolId = UUID.randomUUID();
        Protocol protocol = new Protocol();
        protocol.setProtocol_id(protocolId);
        protocol.setProtocolTitle("ЭРД");

        ProtocolDTO protocolDTO = new ProtocolDTO();
        protocolDTO.setProtocolId(protocolId);

        List<Protocol> protocolList = new ArrayList<>();
        person.setProtocols(protocolList);

        when(personRepository.findById(personUuid)).thenReturn(Optional.of(person));
        when(protocolRepository.findById(protocolDTO.getProtocolId())).thenReturn(Optional.of(protocol));

        personService.addProtocolToPerson(personUuid, protocolDTO);
    }

    @Test
    void testCurePersonWhenProtocolExist() {
        String protocolTitle = "ЭРД";

        List<Protocol> protocolList = new ArrayList<>();
        protocolList.add(protocol);
        person.setProtocols(protocolList);

        when(protocolRepository.findByProtocolTitle(protocolTitle)).thenReturn(Optional.of(protocol));
        when(personRepository.findById(personUuid)).thenReturn(Optional.of(person));

        personService.curePerson(personUuid, protocolTitle);
    }

    @Test
    void testCurePersonWhenProtocolNotFound() {
        String protocolTitle = "ИРД";

        List<Protocol> protocolList = new ArrayList<>();
        protocolList.add(protocol);
        person.setProtocols(protocolList);

        when(protocolRepository.findByProtocolTitle(protocolTitle)).thenReturn(Optional.of(protocol));
        when(personRepository.findById(personUuid)).thenReturn(Optional.of(person));

        Exception exception = assertThrows(NutritionExceptionNotFound.class, () -> personService.curePerson(personUuid, protocolTitle));
        assertEquals("У человека нет такого протокола", exception.getMessage());
    }

    @Test
    void testGetProtocolByIdSuccess() {
        when(protocolRepository.findById(protocolUuid)).thenReturn(Optional.of(protocol));
        when(protocolMapper.toProtocolDTO(protocol)).thenReturn(protocolDTOActual);

        personService.getProtocolById(protocolUuid);

        ProtocolDTO protocolDTOExpected = new ProtocolDTO();
        protocolDTOExpected.setProtocolId(protocolUuid);
        protocolDTOExpected.setProtocolTitle("ЭРД");

        assertEquals(protocolDTOExpected, protocolDTOActual);

        verify(protocolRepository, times(1)).findById(protocolUuid);
    }

    @Test
    void testGetProtocolByIdWhenProtocolNotFound() {
        UUID illegalUuid = UUID.randomUUID();

        when(protocolRepository.findById(illegalUuid)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NutritionExceptionNotFound.class, () -> personService.getProtocolById(illegalUuid));
        assertEquals("Протокола с таким идентификационным номером не найдено", exception.getMessage());

        verify(protocolRepository, times(1)).findById(illegalUuid);
    }

    @Test
    void testAddProtocol() {
        when(protocolMapper.toProtocol(protocolDTOActual)).thenReturn(protocol);
        personService.addProtocol(protocolDTOActual);
        verify(protocolRepository, times(1)).save(Mockito.any(Protocol.class));
    }

    @Test
    void testThrowExceptionIfProtocolAlreadyExist() {
        ProtocolDTO protocolDTO = new ProtocolDTO();
        protocolDTO.setProtocolId(protocolUuid);
        protocolDTO.setProtocolTitle("ЭРД");

        when(protocolRepository.findByProtocolTitle(protocolDTO.getProtocolTitle())).thenReturn(Optional.of(protocol));
        Exception exception = assertThrows(NutritionException.class, () -> personService.throwExceptionIfProtocolAlreadyExist(protocolDTO));
        assertEquals("Такой протокол уже есть в БД", exception.getMessage());

        verify(protocolRepository, times(1)).findByProtocolTitle(protocolDTO.getProtocolTitle());
    }
}