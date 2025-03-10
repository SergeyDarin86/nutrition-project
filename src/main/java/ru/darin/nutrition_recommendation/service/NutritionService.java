package ru.darin.nutrition_recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.nutrition_recommendation.dto.IllnessDTO;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.mapper.IllnessMapper;
import ru.darin.nutrition_recommendation.mapper.PersonMapper;
import ru.darin.nutrition_recommendation.model.Illness;
import ru.darin.nutrition_recommendation.model.Person;
import ru.darin.nutrition_recommendation.repository.IllnessRepository;
import ru.darin.nutrition_recommendation.repository.PersonRepository;
import ru.darin.nutrition_recommendation.util.exception.NutritionException;
import ru.darin.nutrition_recommendation.util.exception.NutritionExceptionNotFound;

import java.util.*;

//TODO: описать проблему с Lombok (какую зависимость нужно поставить в pom.xml)
@Service
@RequiredArgsConstructor
public class NutritionService {

    private final PersonRepository personRepository;

    private final IllnessRepository illnessRepository;

    private final PersonMapper personMapper;

    private final IllnessMapper illnessMapper;

    private final String PERSON_NOT_FOUND_MSG = "Пользователь не найден";

    private final String ILLNESS_WITH_TITLE_NOT_FOUND_MSG = "Заболевания с таким названием не найдено";

    private final String ILLNESS_WITH_ID_NOT_FOUND_MSG = "Заболевания с таким идентификационным номером не найдено";

    private final String ILLNESS_IS_ALREADY_EXIST_MSG = "Такое заболевание уже есть в БД";

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

    @Transactional
    public PersonDTO updatePersonById(UUID id, PersonDTO personDTO) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PERSON_NOT_FOUND_MSG));

        Person updatedPerson = personMapper.toPerson(personDTO);
        updatedPerson.setPersonId(id);
        updatedPerson.setIllnesses(person.getIllnesses());
        personRepository.save(updatedPerson);
        return personMapper.toPersonDto(updatedPerson);
    }

    public void deletePersonById(UUID id){
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PERSON_NOT_FOUND_MSG));
        personRepository.delete(person);
    }

    public Illness getIllnessFromRepoByTitle(PersonDTO personDTO) {
        return illnessRepository
                .findByIllnessTitle(personDTO.getIllnesses().stream().findFirst().get().getIllnessTitle())
                .orElseThrow(() -> new NutritionExceptionNotFound(ILLNESS_WITH_TITLE_NOT_FOUND_MSG));
    }

    @Transactional
    public PersonDTO addIllnessToPerson(UUID id, PersonDTO personDTO) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PERSON_NOT_FOUND_MSG));

        Person updatedPerson = personMapper.toPerson(personDTO);
        updatedPerson.setPersonId(id);

        Illness illness = getIllnessFromRepoByTitle(personDTO);
        Optional<Illness> optional = person.getIllnesses()
                .stream().filter(s -> s.getIllnessTitle().equals(illness.getIllnessTitle())).findFirst();

        if (optional.isEmpty()) {
            List<Illness> illnessList = new ArrayList<>(person.getIllnesses());
            illnessList.add(illness);
            updatedPerson.setIllnesses(illnessList);
        } else {
            updatedPerson.setIllnesses(person.getIllnesses());
        }

        personRepository.saveAndFlush(updatedPerson);

        return personMapper.toPersonDto(updatedPerson);
    }

    public IllnessDTO addIllness(IllnessDTO illnessDTO) {
        throwExceptionIfIllnessAlreadyExist(illnessDTO);
        Illness illness = illnessMapper.toIllness(illnessDTO);
        illnessRepository.save(illness);
        return illnessMapper.toIllnessDTO(illness);
    }

    @Transactional
    public IllnessDTO updateIllnessById(UUID id, IllnessDTO illnessDTO) {
        illnessRepository.findById(id).orElseThrow(() -> new NutritionExceptionNotFound(ILLNESS_WITH_ID_NOT_FOUND_MSG));
        throwExceptionIfIllnessAlreadyExist(illnessDTO);

        Illness updatedIllness = illnessMapper.toIllness(illnessDTO);
        updatedIllness.setIllness_id(id);

        illnessRepository.save(updatedIllness);
        return illnessMapper.toIllnessDTO(updatedIllness);
    }

    //не удаляет данные по заболеванию, если в связанной таблице "person_illness" есть записи
    public void deleteIllnessById(UUID id){
        Illness illness = illnessRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(ILLNESS_WITH_ID_NOT_FOUND_MSG));

        illnessRepository.delete(illness);
    }

    public void throwExceptionIfIllnessAlreadyExist(IllnessDTO illnessDTO) {
        if (illnessRepository.findByIllnessTitle(illnessDTO.getIllnessTitle()).isPresent()) {
            throw new NutritionException(ILLNESS_IS_ALREADY_EXIST_MSG);
        }
    }

    public List<IllnessDTO> getAllIllnesses() {
        return illnessRepository.findAll().stream().map(illnessMapper::toIllnessDTO).toList();
    }

}