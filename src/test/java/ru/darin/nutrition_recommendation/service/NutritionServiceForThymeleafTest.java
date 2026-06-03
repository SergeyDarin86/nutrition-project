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
import ru.darin.nutrition_recommendation.dto.*;
import ru.darin.nutrition_recommendation.mapper.*;
import ru.darin.nutrition_recommendation.model.*;
import ru.darin.nutrition_recommendation.repository.*;
import ru.darin.nutrition_recommendation.util.RecommendationResponseWithDTO;
import ru.darin.nutrition_recommendation.util.exception.NutritionException;
import ru.darin.nutrition_recommendation.util.exception.NutritionExceptionNotFound;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    ProductTypeMapper productTypeMapper;

    @Mock
    ProductTypeRepository productTypeRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductMapper productMapper;

    @Mock
    AllergenTypeMapper allergenTypeMapper;

    @Mock
    AllergenTypeRepository allergenTypeRepository;

    @Mock
    MixRepository mixRepository;

    @Mock
    MIxMapper mIxMapper;

    @InjectMocks
    private NutritionServiceForThymeleaf personService;

    private UUID personUuid;

    private Person person;

    private PersonDTO personDTOActual;

    private Protocol protocol;

    private Protocol protocolTwo;

    private UUID protocolUuidTwo;

    private ProtocolDTO protocolDTOActual;

    private UUID protocolUuid;

    private List<PersonDTO> personDTOListActual;

    private List<ProtocolDTO> protocolDTOListActual;

    private ProductType productType;

    private UUID productTypeUuid;

    private ProductTypeDTO productTypeDTOActual;

    private List<ProductTypeDTO> productTypeDTOListActual;

    private UUID productUuid;

    private Product product;

    private ProductDTO productDTOActual;

    private List<ProductDTO> productDTOListActual;

    private UUID allergenTypeUuid;

    private AllergenType allergenType;

    private AllergenTypeDTO allergenTypeDTOActual;

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

        protocolUuidTwo = UUID.randomUUID();
        protocolTwo = new Protocol();
        protocolTwo.setProtocol_id(protocolUuidTwo);
        protocolTwo.setProtocolTitle("Антикандида");

        protocolDTOActual = new ProtocolDTO();
        protocolDTOActual.setProtocolId(protocolUuid);
        protocolDTOActual.setProtocolTitle("ЭРД");

        protocolDTOListActual = new ArrayList<>();
        protocolDTOListActual.add(protocolDTOActual);

        productTypeUuid = UUID.randomUUID();
        productType = new ProductType();
        productType.setProductTypeId(productTypeUuid);
        productType.setProductType("Крупы");

        productTypeDTOActual = new ProductTypeDTO();
        productTypeDTOActual.setProductTypeId(productTypeUuid);
        productTypeDTOActual.setProductType("Крупы");

        productTypeDTOListActual = new ArrayList<>();
        productTypeDTOListActual.add(productTypeDTOActual);

        productUuid = UUID.randomUUID();
        product = new Product();
        product.setProduct_id(productUuid);
        product.setProductType(productType);
        product.setProduct("Гречка");
        productDTOActual = new ProductDTO();
        productDTOActual.setProductId(productUuid);
        productDTOActual.setProduct("Гречка");
        productDTOActual.setProductTypeDTO(productTypeDTOActual);

        productDTOListActual = new ArrayList<>();
        productDTOListActual.add(productDTOActual);

        allergenTypeUuid = UUID.randomUUID();
        allergenType = new AllergenType();
        allergenType.setAllergenId(allergenTypeUuid);
        allergenType.setAllergenTitle("Цитрусовые");

        allergenTypeDTOActual = new AllergenTypeDTO(allergenTypeUuid, "Цитрусовые");
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

    @Test
    void testDeleteProtocolById() {
        when(protocolRepository.findById(protocolUuid)).thenReturn(Optional.of(protocol));

        personService.deleteProtocolById(protocolUuid);

        verify(protocolRepository, times(1)).deleteFromPersonProtocolTableAndProtocolTableByProtocolId(protocolUuid);
    }

    @Test
    void testUpdateProtocolById() {
        ProtocolDTO updatedProtocolDTO = new ProtocolDTO();
        updatedProtocolDTO.setProtocolId(protocolUuid);
        updatedProtocolDTO.setProtocolTitle("Антикандида");

        Protocol protocolToSave = new Protocol();
        protocolToSave.setProtocol_id(protocolUuid);
        protocolToSave.setProtocolTitle("Антикандида");

        when(protocolRepository.findById(protocolUuid)).thenReturn(Optional.of(protocol));
        when(protocolMapper.toProtocol(updatedProtocolDTO)).thenReturn(protocolToSave);

        personService.updateProtocolById(protocolUuid, updatedProtocolDTO);

        verify(protocolRepository, times(1)).save(Mockito.any(Protocol.class));
    }

    @Test
    void testGetAllProtocols() {
        List<ProtocolDTO> protocolDTOListExpected = new ArrayList<>();
        ProtocolDTO protocolDTO = new ProtocolDTO();
        protocolDTO.setProtocolId(protocolUuid);
        protocolDTO.setProtocolTitle("ЭРД");
        protocolDTOListExpected.add(protocolDTO);

        personService.getAllProtocols();

        assertEquals(protocolDTOListExpected, protocolDTOListActual);
        verify(protocolRepository, times(1)).findAll(Sort.by("protocolTitle"));
    }

    @Test
    void testAddProductType() {
        when(productTypeMapper.toProductType(productTypeDTOActual)).thenReturn(productType);
        personService.addProductType(productTypeDTOActual);
        verify(productTypeRepository, times(1)).save(Mockito.any(ProductType.class));
    }

    @Test
    void testThrowExceptionIfProductTypeAlreadyExist() {
        ProductTypeDTO productTypeDTO = new ProductTypeDTO();
        productTypeDTO.setProductTypeId(productTypeUuid);
        productTypeDTO.setProductType("Крупы");

        when(productTypeRepository.findByProductType(productTypeDTO.getProductType())).thenReturn(Optional.of(productType));
        Exception exception = assertThrows(NutritionException.class, () -> personService.throwExceptionIfProductTypeAlreadyExist(productTypeDTO));
        assertEquals("Такой тип продукта уже есть в БД", exception.getMessage());

        verify(productTypeRepository, times(1)).findByProductType(productTypeDTO.getProductType());
    }

    @Test
    void testGetProductTypeByIdSuccess() {
        when(productTypeRepository.findById(productTypeUuid)).thenReturn(Optional.of(productType));
        when(productTypeMapper.toProductTypeDTO(productType)).thenReturn(productTypeDTOActual);

        personService.getProductTypeById(productTypeUuid);

        ProductTypeDTO productTypeDTOExpected = new ProductTypeDTO();
        productTypeDTOExpected.setProductTypeId(productTypeUuid);
        productTypeDTOExpected.setProductType("Крупы");

        assertEquals(productTypeDTOExpected, productTypeDTOActual);

        verify(productTypeRepository, times(1)).findById(productTypeUuid);
    }

    @Test
    void testProductTypeByIdWhenPersonNotFound() {
        UUID illegalUuid = UUID.randomUUID();

        when(productTypeRepository.findById(illegalUuid)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NutritionExceptionNotFound.class, () -> personService.getProductTypeById(illegalUuid));
        assertEquals("Тип продуктов питания с таким идентификационным номером не найден", exception.getMessage());

        verify(productTypeRepository, times(1)).findById(illegalUuid);
    }


    @Test
    void testDeleteProductTypeById() {
        when(productTypeRepository.findById(productTypeUuid)).thenReturn(Optional.of(productType));
        personService.deleteProductTypeById(productTypeUuid);
        verify(productTypeRepository, times(1)).delete(productType);
    }


    @Test
    void testUpdateProductTypeById() {
        ProductTypeDTO updatedProductTypeDTO = new ProductTypeDTO();
        updatedProductTypeDTO.setProductTypeId(productTypeUuid);
        updatedProductTypeDTO.setProductType("Фрукты");

        ProductType productTypeToSave = new ProductType();
        productTypeToSave.setProductTypeId(productTypeUuid);
        productTypeToSave.setProductType("Фрукты");

        when(productTypeRepository.findById(productTypeUuid)).thenReturn(Optional.ofNullable(productType));
        when(productTypeMapper.toProductType(updatedProductTypeDTO)).thenReturn(productTypeToSave);

        personService.updateProductTypeById(productTypeUuid, updatedProductTypeDTO);
    }

    @Test
    void testGetAllProductTypes() {
        List<ProductTypeDTO> productTypeDTOListExpected = new ArrayList<>();
        ProductTypeDTO productTypeDTO = new ProductTypeDTO();
        productTypeDTO.setProductTypeId(productTypeUuid);
        productTypeDTO.setProductType("Крупы");
        productTypeDTOListExpected.add(productTypeDTO);

        personService.getAllProductTypes();

        assertEquals(productTypeDTOListExpected, productTypeDTOListActual);
        verify(productTypeRepository, times(1)).findAll(Sort.by("productType"));
    }

    @Test
    void testAddProduct() {
        when(productMapper.toProduct(productDTOActual)).thenReturn(product);
        when(productTypeRepository.findById(productTypeUuid)).thenReturn(Optional.of(productType));

        Product savedProduct = product;
        when(productRepository.save(product)).thenReturn(savedProduct);

        when(allergenTypeRepository.findById(allergenTypeUuid)).thenReturn(Optional.of(allergenType));
        when(allergenTypeMapper.toAllergenTypeDTO(allergenType)).thenReturn(allergenTypeDTOActual);

        List<UUID> selectedAllergens = new ArrayList<>();
        selectedAllergens.add(allergenTypeUuid);

        personService.addProduct(productDTOActual, productTypeUuid, selectedAllergens);
        verify(productRepository, times(1)).save(savedProduct);
    }

    @Test
    void testThrowExceptionIfProductAlreadyExist() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(productUuid);
        productDTO.setProductTypeDTO(productTypeDTOActual);
        productDTO.setProduct("Гречка");

        when(productRepository.findByProduct(productDTO.getProduct())).thenReturn(Optional.of(product));
        Exception exception = assertThrows(NutritionException.class, () -> personService.throwExceptionIfProductAlreadyExist(productDTO));
        assertEquals("Такой продукт уже есть в БД", exception.getMessage());

        verify(productRepository, times(1)).findByProduct(productDTO.getProduct());
    }

    @Test
    void testDeleteProductById() {
        when(productRepository.findById(productUuid)).thenReturn(Optional.of(product));
        personService.deleteProductById(productUuid);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testGetProductByIdSuccess() {
        when(productRepository.findById(productUuid)).thenReturn(Optional.of(product));
        personService.getProductById(productUuid);
        verify(productRepository, times(1)).findById(productUuid);
    }

    @Test
    void testGetProductByIdWhenIdNotFound() {
        when(productRepository.findById(productUuid)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NutritionExceptionNotFound.class, () -> personService.getProductById(productUuid));
        assertEquals("Продукт питания с таким идентификационным номером не найден", exception.getMessage());
    }

    @Test
    void testUpdateProductById() {
        ProductDTO productDTOExpected = new ProductDTO();
        productDTOExpected.setProductId(productUuid);
        productDTOExpected.setProductTypeDTO(productTypeDTOActual);
        productDTOExpected.setProduct("Гречка");

        when(productRepository.findById(productUuid)).thenReturn(Optional.of(product));
        List<UUID> selectedAllergens = new ArrayList<>();
        selectedAllergens.add(allergenTypeUuid);

        when(allergenTypeRepository.findById(allergenTypeUuid)).thenReturn(Optional.of(allergenType));
        when(productMapper.toProductDTO(product)).thenReturn(productDTOActual);

        personService.updateProductById(productUuid, productDTOActual, selectedAllergens);
        assertEquals(productDTOExpected, productDTOActual);

    }

    @Test
    void testThrowExceptionIfProductAlreadyExistWithUUIDListOfAllergensForUpdate() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(productUuid);
        productDTO.setProductTypeDTO(productTypeDTOActual);
        productDTO.setProduct("Гречка");

        when(productRepository.findByProduct(productDTO.getProduct())).thenReturn(Optional.of(product));
        Exception exception = assertThrows(NutritionException.class, () -> personService.throwExceptionIfProductAlreadyExistWithUUIDListOfAllergensForUpdate(productDTO, null));
        assertEquals("Такой продукт уже есть в БД", exception.getMessage());

        verify(productRepository, times(1)).findByProduct(productDTO.getProduct());
    }

    @Test
    void testGetAllProducts() {
        List<ProductDTO> productDTOListExpected = new ArrayList<>();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(productUuid);
        productDTO.setProduct("Гречка");
        productDTO.setProductTypeDTO(productTypeDTOActual);
        productDTOListExpected.add(productDTO);

        personService.getAllProducts();

        assertEquals(productDTOListExpected, productDTOListActual);
        verify(productRepository, times(1)).findAll(Sort.by("product"));
    }

    @Test
    void testGetProtocolWithProductsGroupedByType() {
        String protocolString = "ЭРД";
        String resolution = "РАЗРЕШЕНО";

        Mix mix = new Mix();
        mix.setProtocol(protocol);
        mix.setProduct(product);
        mix.setResolution(Resolution.РАЗРЕШЕНО);

        when(mixRepository.findAll()).thenReturn(List.of(mix));
        when(productMapper.toProductDTO(product)).thenReturn(productDTOActual);

        RecommendationResponseWithDTO responseActual = personService.getProtocolWithProductsGroupedByType(protocolString, resolution);

        RecommendationResponseWithDTO responseExpected = new RecommendationResponseWithDTO();
        responseExpected.setProtocol(protocolString);
        responseExpected.setResolution(resolution);
        responseExpected.setProducts(List.of(Map.of("Крупы", List.of(productDTOActual))));

        assertEquals(responseExpected, responseActual);
    }

    @Test
    void testGetMixOfProductsForOneOrTwoProtocols() {
        String protocolOneString = "ЭРД";
        String protocolTwoString = "Антикандида";
        String resolution = "РАЗРЕШЕНО";

        when(protocolRepository.findByProtocolTitle(protocolOneString)).thenReturn(Optional.of(protocol));
        when(protocolRepository.findByProtocolTitle(protocolTwoString)).thenReturn(Optional.of(protocolTwo));
        when(mIxMapper.toResolutionEnum(resolution)).thenReturn(Resolution.РАЗРЕШЕНО);

        RecommendationResponseWithDTO responseActual = personService.getMixOfProductsForOneOrTwoProtocols(protocol.getProtocolTitle(), protocolTwo.getProtocolTitle(), resolution);

        RecommendationResponseWithDTO responseExpected = new RecommendationResponseWithDTO();
        responseExpected.setResolution(resolution);
        responseExpected.setProtocol("ЭРД и Антикандида");
        responseExpected.setProducts(List.of(Map.of()));

        assertEquals(responseExpected, responseActual);
    }

    @Test
    void testFillingMapOfProductsGroupedByTypeWithDTO() {
        Map<String,List<ProductDTO>>productsGroupedByType = new HashMap<>();
        productsGroupedByType.put("Крупы",List.of(productDTOActual));

        Set<Mix>mixForProtocols = new HashSet<>();
        Mix mix = new Mix();
        mix.setProduct(product);
        mix.setProtocol(protocol);
        mixForProtocols.add(mix);

        when(productMapper.toProductDTO(mix.getProduct())).thenReturn((productDTOActual));
        personService.fillingMapOfProductsGroupedByTypeWithDTO(productsGroupedByType,mixForProtocols);

        assertEquals(1, productsGroupedByType.size());
        assertTrue(productsGroupedByType.containsKey("Крупы"));
        assertEquals(1, productsGroupedByType.get("Крупы").size());
        assertEquals(productDTOActual, productsGroupedByType.get("Крупы").get(0));
    }
}