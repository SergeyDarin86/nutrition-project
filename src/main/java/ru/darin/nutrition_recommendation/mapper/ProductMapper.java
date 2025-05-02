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
//    @Mapping(target = "productType", source = "productTypeDTO")
    @Mapping(target = "product_id", source = "productId")
    Product toProduct(ProductDTO productDTO);

    @Mapping(target = "product", source = "product")
    //из-за этого маппинга не отображается страница с продуктами
//    @Mapping(target = "productTypeDTO", source = "productType")
    @Mapping(target = "productId", source = "product_id")
    ProductDTO toProductDTO(Product product);

}