package ru.darin.nutrition_recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.model.Person;
import ru.darin.nutrition_recommendation.repository.PersonRepository;

import java.util.List;

//TODO: описать проблему с Lombok (какую зависимость нужно поставить в pom.xml)
@Service
@RequiredArgsConstructor
public class NutritionService {

    private final PersonRepository personRepository;

    public List<Person>getAllPeople(){
        return personRepository.findAll();
    }

    public void addPerson(PersonDTO personDTO){
        Person person = new Person();
        person.setFullName(personDTO.getFullName());
        personRepository.save(person);
    }

}
