package ru.darin.nutrition_recommendation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.darin.nutrition_recommendation.dto.IllnessDTO;
import ru.darin.nutrition_recommendation.model.Illness;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IllnessMapper {

    @Mapping(source = "illnessTitle",target = "illnessTitle")
    Illness toIllness(IllnessDTO illnessDTO);

    @Mapping(source = "illnessTitle",target = "illnessTitle")
    IllnessDTO toIllnessDTO(Illness illness);

}
