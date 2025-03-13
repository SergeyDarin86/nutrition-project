package ru.darin.nutrition_recommendation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.darin.nutrition_recommendation.dto.ProductTypeDTO;
import ru.darin.nutrition_recommendation.model.ProductType;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProductTypeMapper {

    @Mapping(target = "productType", source = "productType")
    ProductType toProductType(ProductTypeDTO productTypeDTO);

    @Mapping(target = "productType", source = "productType")
    ProductTypeDTO toProductTypeDTO(ProductType productType);

}
