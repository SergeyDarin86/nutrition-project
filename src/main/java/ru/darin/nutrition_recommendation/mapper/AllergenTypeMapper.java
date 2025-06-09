package ru.darin.nutrition_recommendation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.darin.nutrition_recommendation.dto.AllergenTypeDTO;
import ru.darin.nutrition_recommendation.model.AllergenType;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AllergenTypeMapper {

    @Mapping(source = "allergenTitle", target = "allergenTitle")
    @Mapping(target = "allergenId", source = "allergenId")
    @Mapping(target = "titleColor", source = "titleColor")
    AllergenType toAllergenType(AllergenTypeDTO dto);

    @Mapping(source = "allergenTitle", target = "allergenTitle")
    @Mapping(target = "allergenId", source = "allergenId")
    @Mapping(target = "titleColor", source = "titleColor")
    AllergenTypeDTO toAllergenTypeDTO(AllergenType allergenType);

}