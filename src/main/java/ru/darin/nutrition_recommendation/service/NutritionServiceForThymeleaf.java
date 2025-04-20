package ru.darin.nutrition_recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.nutrition_recommendation.dto.*;
import ru.darin.nutrition_recommendation.mapper.*;
import ru.darin.nutrition_recommendation.model.*;
import ru.darin.nutrition_recommendation.repository.*;
import ru.darin.nutrition_recommendation.util.RecommendationResponse;
import ru.darin.nutrition_recommendation.util.exception.NutritionException;
import ru.darin.nutrition_recommendation.util.exception.NutritionExceptionNotFound;
import ru.darin.nutrition_recommendation.util.validation.NutritionValidation;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionServiceForThymeleaf {
    private final PersonRepository personRepository;

    private final IllnessRepository illnessRepository;

    private final ProductTypeRepository productTypeRepository;

    private final ProductRepository productRepository;

    private final MixRepository mixRepository;

    private final PersonMapper personMapper;

    private final IllnessMapper illnessMapper;

    private final ProductTypeMapper productTypeMapper;

    private final ProductMapper productMapper;

    private final MIxMapper mIxMapper;

    private final String PERSON_NOT_FOUND_MSG = "Пользователь не найден";

    private final String PERSON_DOES_NOT_HAVE_THIS_ILLNESS_MSG = "У человека нет такого заболевания";

    private final String ILLNESS_WITH_TITLE_NOT_FOUND_MSG = "Заболевания с таким названием не найдено";

    private final String ILLNESS_WITH_ID_NOT_FOUND_MSG = "Заболевания с таким идентификационным номером не найдено";

    private final String ILLNESS_IS_ALREADY_EXIST_MSG = "Такое заболевание уже есть в БД";

    private final String PRODUCT_IS_ALREADY_EXIST_MSG = "Такой продукт уже есть в БД";

    private final String PRODUCT_TYPE_IS_ALREADY_EXIST_MSG = "Такой тип продукт уже есть в БД";

    private final String PRODUCT_TYPE_NOT_FOUND_MSG = "Такой тип продуктов питания не найден";

    private final String PRODUCT_TYPE_WITH_ID_NOT_FOUND_MSG = "Тип продуктов питания с таким идентификационным номером не найден";

    private final String PRODUCT_WITH_ID_NOT_FOUND_MSG = "Продукт питания с таким идентификационным номером не найден";

    private final String PRODUCT_WITH_TITLE_NOT_FOUND_MSG = "Продукт питания с таким названием не найден";

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
    public PersonDTO addIllnessToPerson(UUID personId, IllnessDTO illnessDTO) {
        Person person = findPersonByIdFromRepo(personId);

        Person updatedPerson = new Person();
        updatedPerson.setPersonId(personId);
        updatedPerson.setFullName(person.getFullName());

        Illness illness = illnessRepository.findById(illnessDTO.getIllnessId()).get();
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

    public Person findPersonByIdFromRepo(UUID id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PERSON_NOT_FOUND_MSG));
    }

    @Transactional
    public PersonDTO curePerson(UUID personId) {
        Person person = findPersonByIdFromRepo(personId);

        Person updatedPerson = new Person();
        updatedPerson.setPersonId(personId);
        updatedPerson.setFullName(person.getFullName());

        Illness illness = getIllnessFromRepoByTitleNew(person);
        System.out.println(illness.getIllnessTitle());
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

    public Illness getIllnessFromRepoByTitleNew(Person person) {
        return illnessRepository
                .findByIllnessTitle(person.getIllnesses().stream().findFirst().get().getIllnessTitle())
                .orElseThrow(() -> new NutritionExceptionNotFound(ILLNESS_WITH_TITLE_NOT_FOUND_MSG));
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

    public IllnessDTO getIllnessById(UUID id) {
        return illnessMapper.toIllnessDTO(illnessRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(ILLNESS_WITH_ID_NOT_FOUND_MSG)));
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

    public ProductTypeDTO addProductType(ProductTypeDTO productTypeDTO) {
        throwExceptionIfProductTypeAlreadyExist(productTypeDTO);
        ProductType productType = productTypeMapper.toProductType(productTypeDTO);
        productTypeRepository.save(productType);
        return productTypeMapper.toProductTypeDTO(productType);
    }

    public ProductTypeDTO getProductTypeById(UUID uuid) {
        return productTypeMapper.toProductTypeDTO(productTypeRepository.findById(uuid)
                .orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_TYPE_WITH_ID_NOT_FOUND_MSG)));
    }

    public void deleteProductTypeById(UUID id) {
        productTypeRepository.delete(productTypeRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_TYPE_WITH_ID_NOT_FOUND_MSG)));
    }

    @Transactional
    public ProductTypeDTO updateProductTypeById(UUID id, ProductTypeDTO productTypeDTO) {
        ProductType productType = productTypeRepository.findById(id).orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_TYPE_WITH_ID_NOT_FOUND_MSG));
        throwExceptionIfProductTypeAlreadyExist(productTypeDTO);
        productType.setProductType(productTypeDTO.getProductType());
        return productTypeMapper.toProductTypeDTO(productType);
    }

    public List<ProductTypeDTO> getAllProductTypes() {
        return productTypeRepository.findAll().stream().map(productTypeMapper::toProductTypeDTO).toList();
    }

    public void throwExceptionIfProductTypeAlreadyExist(ProductTypeDTO productTypeDTO) {
        if (productTypeRepository.findByProductType(productTypeDTO.getProductType()).isPresent()) {
            throw new NutritionException(PRODUCT_TYPE_IS_ALREADY_EXIST_MSG);
        }
    }

    public ProductDTO addProduct(ProductDTO productDTO, UUID productTypeId) {
        throwExceptionIfProductAlreadyExist(productDTO);
        Product product = productMapper.toProduct(productDTO);
        ProductType productType = productTypeRepository.findById(productTypeId).orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_TYPE_WITH_ID_NOT_FOUND_MSG));
        product.setProductType(productType);
        productRepository.save(product);
        return productMapper.toProductDTO(product);
    }

    public void deleteProductById(UUID id) {
        productRepository.delete(productRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_WITH_ID_NOT_FOUND_MSG)));
    }

    public ProductDTO getProductById(UUID id) {
        return productMapper.toProductDTO(productRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_WITH_ID_NOT_FOUND_MSG)));
    }

    @Transactional
    public ProductDTO updateProductById(UUID id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_WITH_ID_NOT_FOUND_MSG));
        throwExceptionIfProductAlreadyExist(productDTO);
        product.setProduct(productDTO.getProduct());
        return productMapper.toProductDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll(Sort.by("product")).stream().map(productMapper::toProductDTO).toList();
    }

    public void throwExceptionIfProductAlreadyExist(ProductDTO productDTO) {
        if (productRepository.findByProduct(productDTO.getProduct()).isPresent()) {
            throw new NutritionException(PRODUCT_IS_ALREADY_EXIST_MSG);
        }
    }

    // метод нахождения микса продуктов для одного заболевания (РАЗРЕШЕНО/ЗАПРЕЩЕНО)
    public RecommendationResponse getIllnessWithProductsGroupedByType(String illness, String resolution) {

        RecommendationResponse response = new RecommendationResponse();
        Map<String, List<String>> productsGroupedByType = new HashMap<>();

        response.setIllness(illness);
        response.setResolution(resolution);

        List<Map<String, List<String>>> productsForIllness = new ArrayList<>();

        mixRepository.findAll().stream()
                .filter(mix -> illness.equals(mix.getIllness().getIllnessTitle()) && mix.getResolution().toString().equals(resolution))
                .forEach(mix -> productsGroupedByType.put(mix.getProduct().getProductType().getProductType(), new ArrayList<>()));

        for (Mix mix : mixRepository.findAll()) {
            if (!(illness.equals(mix.getIllness().getIllnessTitle()) && mix.getResolution().toString().equals(resolution)))
                continue;
            for (Map.Entry<String, List<String>> listEntry : productsGroupedByType.entrySet()) {
                if (!listEntry.getKey().equals(mix.getProduct().getProductType().getProductType())) continue;
                listEntry.getValue().add(mix.getProduct().getProduct());
            }
        }

        productsForIllness.add(productsGroupedByType);
        response.setProducts(productsForIllness);
        return response;
    }

    // метод нахождения микса РАЗРЕШЕННЫХ продуктов для 2-х заболеваний (а также для одного заболевания)
    @Cacheable("myCash")
    public RecommendationResponse getMixOfProductsForOneOrTwoIllnesses(String illnessOne, String illnessTwo, String resolution) {
        RecommendationResponse response = new RecommendationResponse();
        Map<String, List<String>> productsGroupedByType = new HashMap<>();
        List<Map<String, List<String>>> productsForIllness = new ArrayList<>();

        Set<Mix> mixIllnessOne = getMixOfProductsForSingleIllness(illnessOne, resolution);
        Set<Mix> mixIllnessTwo;
        Set<Mix> mixForIllnesses = new HashSet<>(mixIllnessOne);

        response.setIllness(illnessOne);
        if (!illnessTwo.isEmpty()) {
            response.setIllness(response.getIllness().concat(" и " + illnessTwo));
            mixIllnessTwo = getMixOfProductsForSingleIllness(illnessTwo, resolution);
            if (mIxMapper.toResolutionEnum(resolution).equals(Resolution.РАЗРЕШЕНО)) {
                mixForIllnesses.retainAll(mixIllnessTwo);
            } else {
                mixForIllnesses.addAll(mixIllnessTwo);
            }
        }

        fillingMapOfProductsGroupedByType(productsGroupedByType, mixForIllnesses);
        productsForIllness.add(productsGroupedByType);
        response.setResolution(resolution);
        response.setProducts(productsForIllness);

        return response;
    }

    public void fillingMapOfProductsGroupedByType(Map<String, List<String>> productsGroupedByType, Set<Mix> mixForIllnesses) {
        mixForIllnesses.stream()
                .forEach(mix -> productsGroupedByType.put(mix.getProduct().getProductType().getProductType(), new ArrayList<>()));

        for (Mix mix : mixForIllnesses) {
            for (Map.Entry<String, List<String>> listEntry : productsGroupedByType.entrySet()) {
                if (!listEntry.getKey().equals(mix.getProduct().getProductType().getProductType())) continue;
                listEntry.getValue().add(mix.getProduct().getProduct());
            }
        }
    }

    public Set<Mix> getMixOfProductsForSingleIllness(String illness, String resolution) {
        illnessRepository.findByIllnessTitle(illness).orElseThrow(() -> new NutritionExceptionNotFound(illness + " - " + ILLNESS_WITH_TITLE_NOT_FOUND_MSG));
        return mixRepository.findAll()
                .stream().filter(mix -> mix.getIllness().getIllnessTitle().equals(illness)
                        && mix.getResolution().toString().equals(resolution)).collect(Collectors.toSet());
    }

    public void addMixOfProductsAndIllnesses(MixDTO mixDTO, UUID illnessId, UUID productId) {
        Illness illness = illnessRepository.findById(illnessId).orElseThrow(() -> new NutritionExceptionNotFound(ILLNESS_WITH_ID_NOT_FOUND_MSG));
        Product product = productRepository.findById(productId).orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_WITH_TITLE_NOT_FOUND_MSG));

        ProductIllness complexKey = new ProductIllness(product.getProduct_id(), illness.getIllness_id());

        Resolution resolution = mIxMapper.toResolutionEnum(mixDTO.getResolution());
        Mix mix = new Mix(complexKey, product, illness, resolution);

        mixRepository.save(mix);
    }

}
