package ru.darin.nutrition_recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.mapper.PersonMapper;
import ru.darin.nutrition_recommendation.model.Person;
import ru.darin.nutrition_recommendation.repository.PersonRepository;
import ru.darin.nutrition_recommendation.util.exception.NutritionExceptionNotFound;

import java.util.List;
import java.util.UUID;

//TODO: описать проблему с Lombok (какую зависимость нужно поставить в pom.xml)
@Service
@RequiredArgsConstructor
public class NutritionService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    private final String PERSON_NOT_FOUND_MSG = "Пользователь не найден";

    public List<PersonDTO> getAllPeople() {
        return personRepository.findAll().stream().map(personMapper::toPersonDto).toList();
    }

    public PersonDTO addPerson(PersonDTO personDTO) {
        Person person = personMapper.toPerson(personDTO);
        personRepository.save(person);
        return personMapper.toPersonDto(person);
    }

    public PersonDTO getPersonById(UUID uuid) {
        return personMapper.toPersonDto(personRepository.findById(uuid)
                .orElseThrow(() -> new NutritionExceptionNotFound(PERSON_NOT_FOUND_MSG)));
    }

}