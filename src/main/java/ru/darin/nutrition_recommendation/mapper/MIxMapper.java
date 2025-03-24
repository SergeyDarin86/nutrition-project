package ru.darin.nutrition_recommendation.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.darin.nutrition_recommendation.model.Resolution;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MIxMapper {

    @ValueMapping(target = "РАЗРЕШЕНО", source = "РАЗРЕШЕНО")
    @ValueMapping(target = "ЗАПРЕЩЕНО", source = "ЗАПРЕЩЕНО")
    Resolution toResolution(String resolution);

}