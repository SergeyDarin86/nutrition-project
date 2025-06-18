package ru.darin.nutrition_recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.nutrition_recommendation.dto.*;
import ru.darin.nutrition_recommendation.mapper.*;
import ru.darin.nutrition_recommendation.model.*;
import ru.darin.nutrition_recommendation.repository.*;
import ru.darin.nutrition_recommendation.util.RecommendationResponseWithDTO;
import ru.darin.nutrition_recommendation.util.exception.NutritionException;
import ru.darin.nutrition_recommendation.util.exception.NutritionExceptionNotFound;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionServiceForThymeleaf {
    private final PersonRepository personRepository;

    private final ProtocolRepository protocolRepository;

    private final ProductTypeRepository productTypeRepository;

    private final ProductRepository productRepository;

    private final MixRepository mixRepository;

    private final AllergenTypeRepository allergenTypeRepository;

    private final PersonMapper personMapper;

    private final ProtocolMapper protocolMapper;

    private final ProductTypeMapper productTypeMapper;

    private final ProductMapper productMapper;

    private final MIxMapper mIxMapper;

    private final AllergenTypeMapper allergenTypeMapper;

    private final String PERSON_NOT_FOUND_MSG = "Пользователь не найден";

    private final String PERSON_DOES_NOT_HAVE_THIS_PROTOCOL_MSG = "У человека нет такого протокола";

    private final String PROTOCOL_WITH_TITLE_NOT_FOUND_MSG = "Протокола с таким названием не найдено";

    private final String PROTOCOL_WITH_ID_NOT_FOUND_MSG = "Протокола с таким идентификационным номером не найдено";

    private final String PROTOCOL_IS_ALREADY_EXIST_MSG = "Такой протокол уже есть в БД";

    private final String ALLERGEN_TYPE_IS_ALREADY_EXIST_MSG = "Такой тип аллергена уже есть в БД";

    private final String ALLERGEN_TYPE_COLOR_IS_ALREADY_EXIST_MSG = "Такой цвет для аллергена уже используется";

    private final String PRODUCT_IS_ALREADY_EXIST_MSG = "Такой продукт уже есть в БД";

    private final String PRODUCT_TYPE_IS_ALREADY_EXIST_MSG = "Такой тип продукт уже есть в БД";

    private final String PRODUCT_TYPE_NOT_FOUND_MSG = "Такой тип продуктов питания не найден";

    private final String PRODUCT_TYPE_WITH_ID_NOT_FOUND_MSG = "Тип продуктов питания с таким идентификационным номером не найден";

    private final String ALLERGEN_TYPE_WITH_ID_NOT_FOUND_MSG = "Тип аллергена с таким идентификационным номером не найден";

    private final String PRODUCT_WITH_ID_NOT_FOUND_MSG = "Продукт питания с таким идентификационным номером не найден";

    private final String PRODUCT_WITH_TITLE_NOT_FOUND_MSG = "Продукт питания с таким названием не найден";

    public List<PersonDTO> getAllPeople() {
        return personRepository.findAll(Sort.by("fullName")).stream().map(personMapper::toPersonDto).toList();
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
        updatedPerson.setProtocols(person.getProtocols());
        personRepository.save(updatedPerson);
        return personMapper.toPersonDto(updatedPerson);
    }

    public void deletePersonById(UUID id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PERSON_NOT_FOUND_MSG));
        personRepository.delete(person);
    }

    public Protocol getProtocolFromRepoByTitle(PersonDTO personDTO) {
        return protocolRepository
                .findByProtocolTitle(personDTO.getProtocols().stream().findFirst().get().getProtocolTitle())
                .orElseThrow(() -> new NutritionExceptionNotFound(PROTOCOL_WITH_TITLE_NOT_FOUND_MSG));
    }

    @Transactional
    public PersonDTO addProtocolToPerson(UUID personId, ProtocolDTO protocolDTO) {
        Person person = findPersonByIdFromRepo(personId);

        Person updatedPerson = new Person();
        updatedPerson.setPersonId(personId);
        updatedPerson.setFullName(person.getFullName());

        Protocol protocol = protocolRepository.findById(protocolDTO.getProtocolId()).get();
        Optional<Protocol> optional = person.getProtocols()
                .stream().filter(s -> s.getProtocolTitle().equals(protocol.getProtocolTitle())).findFirst();

        if (optional.isEmpty()) {
            List<Protocol> protocolList = new ArrayList<>(person.getProtocols());
            protocolList.add(protocol);
            updatedPerson.setProtocols(protocolList);
        } else {
            updatedPerson.setProtocols(person.getProtocols());
        }
        personRepository.saveAndFlush(updatedPerson);
        return personMapper.toPersonDto(updatedPerson);
    }

    public Person findPersonByIdFromRepo(UUID id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PERSON_NOT_FOUND_MSG));
    }

    @Transactional
    public PersonDTO curePerson(UUID personId, String illnessTitle) {
        Person person = findPersonByIdFromRepo(personId);

        Person updatedPerson = new Person();
        updatedPerson.setPersonId(personId);
        updatedPerson.setFullName(person.getFullName());

        Optional<Protocol> optional = person.getProtocols()
                .stream().filter(s -> s.getProtocolTitle().equals(illnessTitle)).findFirst();

        if (optional.isEmpty()) {
            throw new NutritionExceptionNotFound(PERSON_DOES_NOT_HAVE_THIS_PROTOCOL_MSG);
        } else {
            person.getProtocols().remove(getProtocolFromRepoByTitle(illnessTitle));
            updatedPerson.setProtocols(person.getProtocols());
            personRepository.saveAndFlush(updatedPerson);
        }
        return personMapper.toPersonDto(updatedPerson);
    }

    public Protocol getIllnessFromRepoByTitleNew(Person person) {
        return protocolRepository
                .findByProtocolTitle(person.getProtocols().stream().findFirst().get().getProtocolTitle())
                .orElseThrow(() -> new NutritionExceptionNotFound(PROTOCOL_WITH_TITLE_NOT_FOUND_MSG));
    }

    public ProtocolDTO addProtocol(ProtocolDTO protocolDTO) {
        throwExceptionIfProtocolAlreadyExist(protocolDTO);
        Protocol protocol = protocolMapper.toProtocol(protocolDTO);
        protocolRepository.save(protocol);
        return protocolMapper.toProtocolDTO(protocol);
    }

    @Transactional
    public ProtocolDTO updateProtocolById(UUID id, ProtocolDTO protocolDTO) {
        protocolRepository.findById(id).orElseThrow(() -> new NutritionExceptionNotFound(PROTOCOL_WITH_ID_NOT_FOUND_MSG));
        throwExceptionIfProtocolAlreadyExist(protocolDTO);

        Protocol updatedProtocol = protocolMapper.toProtocol(protocolDTO);
        updatedProtocol.setProtocol_id(id);

        protocolRepository.save(updatedProtocol);
        return protocolMapper.toProtocolDTO(updatedProtocol);
    }

    public ProtocolDTO getProtocolById(UUID id) {
        return protocolMapper.toProtocolDTO(protocolRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PROTOCOL_WITH_ID_NOT_FOUND_MSG)));
    }

    @Transactional
    public void deleteProtocolById(UUID id) {
        protocolRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PROTOCOL_WITH_ID_NOT_FOUND_MSG));

        protocolRepository.deleteFromPersonProtocolTableAndProtocolTableByProtocolId(id);
    }

    public void throwExceptionIfProtocolAlreadyExist(ProtocolDTO protocolDTO) {
        if (protocolRepository.findByProtocolTitle(protocolDTO.getProtocolTitle()).isPresent()) {
            throw new NutritionException(PROTOCOL_IS_ALREADY_EXIST_MSG);
        }
    }

    public List<ProtocolDTO> getAllProtocols() {
        return protocolRepository.findAll(Sort.by("protocolTitle")).stream().map(protocolMapper::toProtocolDTO).toList();
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
        return productTypeRepository.findAll(Sort.by("productType")).stream().map(productTypeMapper::toProductTypeDTO).toList();
    }

    public void throwExceptionIfProductTypeAlreadyExist(ProductTypeDTO productTypeDTO) {
        if (productTypeRepository.findByProductType(productTypeDTO.getProductType()).isPresent()) {
            throw new NutritionException(PRODUCT_TYPE_IS_ALREADY_EXIST_MSG);
        }
    }

    public ProductDTO addProduct(ProductDTO productDTO, UUID productTypeId, List<UUID> selectedAllergens) {
        throwExceptionIfProductAlreadyExist(productDTO);

        Product product = productMapper.toProduct(productDTO);
        ProductType productType = productTypeRepository.findById(productTypeId).orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_TYPE_WITH_ID_NOT_FOUND_MSG));
        product.setProductType(productType);

        Product savedProduct = productRepository.save(product);
        Product productWithAllergens = new Product(savedProduct.getProduct_id(), savedProduct.getProduct(), savedProduct.getProductType(), savedProduct.getMixes());
        productWithAllergens.setAllergenTypes(getAllergenTypeDTOList(selectedAllergens).stream().map(allergenTypeMapper::toAllergenType).toList());
        return productMapper.toProductDTO(productRepository.save(productWithAllergens));
    }

    public void deleteProductById(UUID id) {
        productRepository.delete(productRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_WITH_ID_NOT_FOUND_MSG)));
    }

    public ProductDTO getProductById(UUID id) {
        return productMapper.toProductDTO(productRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_WITH_ID_NOT_FOUND_MSG)));
    }

    //TODO: описать как решил проблему: org.hibernate.persistentobjectexception: detached entity passed to persist: jpa repository
    // убрал CascadeType.Persist - оставил Merge
    @Transactional
    public ProductDTO updateProductById(UUID id, ProductDTO productDTO, List<UUID> selectedAllergens) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_WITH_ID_NOT_FOUND_MSG));
        throwExceptionIfProductAlreadyExistWithUUIDListOfAllergensForUpdate(productDTO, selectedAllergens);
        product.setProduct(productDTO.getProduct());
        if (selectedAllergens != null)
            product.setAllergenTypes(getAllergenTypeDTOList(selectedAllergens).stream().map(allergenTypeMapper::toAllergenType).toList());
        return productMapper.toProductDTO(product);
    }

    public List<AllergenTypeDTO> getAllergenTypeDTOList(List<UUID> selectedAllergens) {
        List<AllergenTypeDTO> allergenTypes = new ArrayList<>();
        if (selectedAllergens != null)
            selectedAllergens.forEach(uuid -> allergenTypes.add(getAllergenTypeById(uuid)));
        return allergenTypes;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll(Sort.by("product")).stream().map(productMapper::toProductDTO).toList();
    }

    public void throwExceptionIfProductAlreadyExistWithUUIDListOfAllergensForUpdate(ProductDTO productDTO, List<UUID> selectedAllergens) {
        if (productRepository.findByProduct(productDTO.getProduct()).isPresent() && selectedAllergens == null) {
            throw new NutritionException(PRODUCT_IS_ALREADY_EXIST_MSG);
        }
    }

    public void throwExceptionIfProductAlreadyExist(ProductDTO productDTO) {
        if (productRepository.findByProduct(productDTO.getProduct()).isPresent()) {
            throw new NutritionException(PRODUCT_IS_ALREADY_EXIST_MSG);
        }
    }

    // метод нахождения микса продуктов для одного заболевания (РАЗРЕШЕНО/ЗАПРЕЩЕНО)
    public RecommendationResponseWithDTO getProtocolWithProductsGroupedByType(String protocol, String resolution) {

        RecommendationResponseWithDTO response = new RecommendationResponseWithDTO();
        Map<String, List<ProductDTO>> productsGroupedByType = new TreeMap<>();

        response.setProtocol(protocol);
        response.setResolution(resolution);

        List<Map<String, List<ProductDTO>>> productsForProtocol = new ArrayList<>();

        mixRepository.findAll().stream()
                .filter(mix -> protocol.equals(mix.getProtocol().getProtocolTitle()) && mix.getResolution().toString().equals(resolution))
                .forEach(mix -> productsGroupedByType.put(mix.getProduct().getProductType().getProductType(), new ArrayList<>()));

        for (Mix mix : mixRepository.findAll()) {
            if (!(protocol.equals(mix.getProtocol().getProtocolTitle()) && mix.getResolution().toString().equals(resolution)))
                continue;
            for (Map.Entry<String, List<ProductDTO>> listEntry : productsGroupedByType.entrySet()) {
                if (!listEntry.getKey().equals(mix.getProduct().getProductType().getProductType())) continue;
                listEntry.getValue().add(productMapper.toProductDTO(mix.getProduct()));
            }
        }

        productsForProtocol.add(productsGroupedByType);
        response.setProducts(productsForProtocol);
        return response;
    }

    // метод нахождения микса РАЗРЕШЕННЫХ продуктов для 2-х заболеваний (а также для одного заболевания)
    // если использовать этот метод, то при удалении продуктов питания, данные остаются в кэше
    // и используется старая информация
//    @Cacheable("myCash")
    public RecommendationResponseWithDTO getMixOfProductsForOneOrTwoProtocols(String protocolOne, String protocolTwo, String resolution) {
        RecommendationResponseWithDTO response = new RecommendationResponseWithDTO();
        Map<String, List<ProductDTO>> productsGroupedByType = new HashMap<>();
        List<Map<String, List<ProductDTO>>> productsForProtocol = new ArrayList<>();

        Set<Mix> mixProtocolOne = getMixOfProductsForSingleProtocol(protocolOne, resolution);
        Set<Mix> mixProtocolTwo;
        Set<Mix> mixForProtocols = new TreeSet<>(mixProtocolOne);

        response.setProtocol(protocolOne);
        if (protocolTwo != null) {
            response.setProtocol(response.getProtocol().concat(" и " + protocolTwo));
            mixProtocolTwo = getMixOfProductsForSingleProtocol(protocolTwo, resolution);
            if (resolution != null) {
                if (mIxMapper.toResolutionEnum(resolution).equals(Resolution.РАЗРЕШЕНО)) {
                    mixForProtocols.retainAll(mixProtocolTwo);
                } else {
                    mixForProtocols.addAll(mixProtocolTwo);
                }
            }
        }

        fillingMapOfProductsGroupedByTypeWithDTO(productsGroupedByType, mixForProtocols);
        productsForProtocol.add(productsGroupedByType);
        response.setResolution(resolution);
        response.setProducts(productsForProtocol);

        return response;
    }

    public void fillingMapOfProductsGroupedByTypeWithDTO(Map<String, List<ProductDTO>> productsGroupedByType, Set<Mix> mixForProtocols) {
        mixForProtocols.stream()
                .forEach(mix -> productsGroupedByType.put(mix.getProduct().getProductType().getProductType(), new ArrayList<>()));

        for (Mix mix : mixForProtocols) {
            for (Map.Entry<String, List<ProductDTO>> listEntry : productsGroupedByType.entrySet()) {
                if (!listEntry.getKey().equals(mix.getProduct().getProductType().getProductType())) continue;
                listEntry.getValue().add(productMapper.toProductDTO(mix.getProduct()));
            }
        }
    }

    public Set<Mix> getMixOfProductsForSingleProtocol(String protocol, String resolution) {
        protocolRepository.findByProtocolTitle(protocol).orElseThrow(() -> new NutritionExceptionNotFound(protocol + " - " + PROTOCOL_WITH_TITLE_NOT_FOUND_MSG));
        return mixRepository.findAll()
                .stream().filter(mix -> mix.getProtocol().getProtocolTitle().equals(protocol)
                        && mix.getResolution().toString().equals(resolution)).collect(Collectors.toSet());
    }

    public void addMixOfProductsAndProtocols(MixDTO mixDTO, UUID protocolId, UUID productId) {
        Protocol protocol = protocolRepository.findById(protocolId).orElseThrow(() -> new NutritionExceptionNotFound(PROTOCOL_WITH_ID_NOT_FOUND_MSG));
        Product product = productRepository.findById(productId).orElseThrow(() -> new NutritionExceptionNotFound(PRODUCT_WITH_TITLE_NOT_FOUND_MSG));

        ProductProtocol complexKey = new ProductProtocol(product.getProduct_id(), protocol.getProtocol_id());

        Resolution resolution = mIxMapper.toResolutionEnum(mixDTO.getResolution());
        Mix mix = new Mix(complexKey, product, protocol, resolution);

        mixRepository.save(mix);
    }

    public Protocol getProtocolFromRepoByTitle(String protocolTitle) {
        return protocolRepository
                .findByProtocolTitle(protocolTitle)
                .orElseThrow(() -> new NutritionExceptionNotFound(PROTOCOL_WITH_TITLE_NOT_FOUND_MSG));
    }

    public void deleteMixOfProductAndIllnessByProductIdWithIllnessId(UUID productId, UUID illnessId) {
        mixRepository.deleteById(new ProductProtocol(productId, illnessId));
    }

    public ProductDTO getProductDTOByProductName(String product) {
        return productMapper.toProductDTO(productRepository.findByProduct(product).get());
    }

    public List<AllergenTypeDTO> getAllergenTypes() {
        return allergenTypeRepository.findAll().stream().map(allergenTypeMapper::toAllergenTypeDTO).toList();
    }

    public AllergenTypeDTO addAllergenType(AllergenTypeDTO allergenTypeDTO) {
        throwExceptionIfAllergenTypeAlreadyExist(allergenTypeDTO);
        throwExceptionIfAllergenTypeColorAlreadyExist(allergenTypeDTO);
        AllergenType allergenType = allergenTypeMapper.toAllergenType(allergenTypeDTO);
        allergenTypeRepository.save(allergenType);
        return allergenTypeMapper.toAllergenTypeDTO(allergenType);
    }

    public AllergenTypeDTO getAllergenTypeById(UUID uuid) {
        return allergenTypeMapper.toAllergenTypeDTO(allergenTypeRepository.findById(uuid)
                .orElseThrow(() -> new NutritionExceptionNotFound("Нет такого аллергена")));
    }

    @Transactional
    public AllergenTypeDTO updateAllergenTypeById(UUID id, AllergenTypeDTO allergenTypeDTO) {
        AllergenType allergenType = allergenTypeRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(ALLERGEN_TYPE_WITH_ID_NOT_FOUND_MSG));
        throwExceptionIfAllergenTypeAlreadyExist(allergenTypeDTO);
        allergenType.setAllergenTitle(allergenTypeDTO.getAllergenTitle());
        return allergenTypeMapper.toAllergenTypeDTO(allergenType);
    }

    //TODO:
    //  + в редактировании продукта добавить изменение типа аллергена
    // в редактировании типа аллергена добавить изменение цвета!?
    // не должно быть повторяющихся значений для цвета
    // добавить сообщение на экран: "Создайте аллерген", если список пустой
    // добавить ограничение на тип поля title_color - не может быть NULL
    // добавить ограничение на количество аллергенов для продукта (не более 2-х)

    public void deleteAllergenTypeById(UUID id) {
        AllergenType allergenType = allergenTypeRepository.findById(id)
                .orElseThrow(() -> new NutritionExceptionNotFound(ALLERGEN_TYPE_WITH_ID_NOT_FOUND_MSG));

        allergenType.getProducts().forEach(product -> product.getAllergenTypes().clear());
        allergenTypeRepository.delete(allergenType);
    }

    public void throwExceptionIfAllergenTypeAlreadyExist(AllergenTypeDTO allergenTypeDTO) {
        if (allergenTypeRepository.findByAllergenTitle(allergenTypeDTO.getAllergenTitle()).isPresent()) {
            throw new NutritionException(ALLERGEN_TYPE_IS_ALREADY_EXIST_MSG);
        }
    }

    public void throwExceptionIfAllergenTypeColorAlreadyExist(AllergenTypeDTO allergenTypeDTO) {
        if (allergenTypeRepository.findByTitleColor(allergenTypeDTO.getTitleColor()).isPresent()) {
            throw new NutritionException(ALLERGEN_TYPE_COLOR_IS_ALREADY_EXIST_MSG);
        }
    }

}