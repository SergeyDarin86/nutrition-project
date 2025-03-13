package ru.darin.nutrition_recommendation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.darin.nutrition_recommendation.dto.ProductDTO;
import ru.darin.nutrition_recommendation.model.Product;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProductMapper {

    @Mapping(target = "product", source = "product")
    @Mapping(target = "productType", source = "productTypeDTO")
    Product toProduct(ProductDTO productDTO);

    @Mapping(target = "product", source = "product")
    @Mapping(target = "productTypeDTO", source = "productType")
    ProductDTO toProductDTO(Product product);

}
