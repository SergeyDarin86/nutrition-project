package ru.darin.nutrition_recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.nutrition_recommendation.dto.IllnessDTO;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.dto.ProductDTO;
import ru.darin.nutrition_recommendation.dto.ProductTypeDTO;
import ru.darin.nutrition_recommendation.mapper.IllnessMapper;
import ru.darin.nutrition_recommendation.mapper.PersonMapper;
import ru.darin.nutrition_recommendation.mapper.ProductMapper;
import ru.darin.nutrition_recommendation.mapper.ProductTypeMapper;
import ru.darin.nutrition_recommendation.model.Illness;
import ru.darin.nutrition_recommendation.model.Person;
import ru.darin.nutrition_recommendation.model.Product;
import ru.darin.nutrition_recommendation.model.ProductType;
import ru.darin.nutrition_recommendation.repository.IllnessRepository;
import ru.darin.nutrition_recommendation.repository.PersonRepository;
import ru.darin.nutrition_recommendation.repository.ProductRepository;
import ru.darin.nutrition_recommendation.repository.ProductTypeRepository;
import ru.darin.nutrition_recommendation.util.exception.NutritionException;
import ru.darin.nutrition_recommendation.util.exception.NutritionExceptionNotFound;

import java.util.*;

//TODO: описать проблему с Lombok (какую зависимость нужно поставить в pom.xml)
// - описать ошибку, которая возникает, если убрать @Transactional при удалении заболевания

@Service
@RequiredArgsConstructor
public class NutritionService {

    private final PersonRepository personRepository;

    private final IllnessRepository illnessRepository;

    private final ProductTypeRepository productTypeRepository;

    private final ProductRepository productRepository;

    private final PersonMapper personMapper;

    private final IllnessMapper illnessMapper;

    private final ProductTypeMapper productTypeMapper;

    private final ProductMapper productMapper;

    private final String PERSON_NOT_FOUND_MSG = "Пользователь не найден";

    private final String PERSON_DOES_NOT_HAVE_THIS_ILLNESS_MSG = "У человека нет такого заболевания";

    private final String ILLNESS_WITH_TITLE_NOT_FOUND_MSG = "Заболевания с таким названием не найдено";

    private final String ILLNESS_WITH_ID_NOT_FOUND_MSG = "Заболевания с таким идентификационным номером не найдено";

    private final String ILLNESS_IS_ALREADY_EXIST_MSG = "Такое заболевание уже есть в БД";

    private final String PRODUCT_TYPE_NOT_FOUND_MSG = "Такой тип продуктов питания не найден";

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

    public void deletePersonById(UUID id) {
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
        Person person = findPersonByIdFromRepo(id);

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

    public Person findPersonByIdFromRepo(UUID id){
        return personRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PERSON_NOT_FOUND_MSG));
    }

    @Transactional
    public PersonDTO curePerson(UUID id, PersonDTO personDTO) {
        Person person = findPersonByIdFromRepo(id);

        Person updatedPerson = personMapper.toPerson(personDTO);
        updatedPerson.setPersonId(id);

        Illness illness = getIllnessFromRepoByTitle(personDTO);
        Optional<Illness> optional = person.getIllnesses()
                .stream().filter(s -> s.getIllnessTitle().equals(illness.getIllnessTitle())).findFirst();

        if (optional.isEmpty()) {
            throw new NutritionExceptionNotFound(PERSON_DOES_NOT_HAVE_THIS_ILLNESS_MSG);
        } else {
            person.getIllnesses().remove(illness);
            updatedPerson.setIllnesses(person.getIllnesses());
            personRepository.saveAndFlush(updatedPerson);
        }
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

    @Transactional
    public void deleteIllnessById(UUID id) {
        illnessRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(ILLNESS_WITH_ID_NOT_FOUND_MSG));

        illnessRepository.deleteFromPersonIllnessTableAndIllnessTableByIllnessId(id);
    }

    public void throwExceptionIfIllnessAlreadyExist(IllnessDTO illnessDTO) {
        if (illnessRepository.findByIllnessTitle(illnessDTO.getIllnessTitle()).isPresent()) {
            throw new NutritionException(ILLNESS_IS_ALREADY_EXIST_MSG);
        }
    }

    public List<IllnessDTO> getAllIllnesses() {
        return illnessRepository.findAll().stream().map(illnessMapper::toIllnessDTO).toList();
    }

    public ProductTypeDTO addProductType(ProductTypeDTO productTypeDTO){
        ProductType productType = productTypeMapper.toProductType(productTypeDTO);
        productTypeRepository.save(productType);
        return productTypeMapper.toProductTypeDTO(productType);
    }

    public ProductDTO addProduct(ProductDTO productDTO){
        Product product = productMapper.toProduct(productDTO);
        ProductType productType = productTypeRepository.findByProductType(productDTO.getProductTypeDTO().getProductType())
                .orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_TYPE_NOT_FOUND_MSG));
        product.setProductType(productType);
        productRepository.save(product);
        return productMapper.toProductDTO(product);
    }

}