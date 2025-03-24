package ru.darin.nutrition_recommendation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.darin.nutrition_recommendation.model.Resolution;
import ru.darin.nutrition_recommendation.util.exception.NutritionException;
import ru.darin.nutrition_recommendation.util.exception.NutritionExceptionNotFound;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MIxMapper {

    String RESOLUTION_IS_NOT_CORRECT_MSG = "Недопустимый тип рекомендаций. Введите: 'РАЗРЕШЕНО' или 'ЗАПРЕЩЕНО'";
    default Resolution toResolution(String resolution) {
        if ( resolution == null ) {
            return null;
        }

        Resolution resolutionEnum;

        switch ( resolution ) {
            case "РАЗРЕШЕНО": resolutionEnum = Resolution.РАЗРЕШЕНО;
                break;
            case "ЗАПРЕЩЕНО": resolutionEnum = Resolution.ЗАПРЕЩЕНО;
                break;
            default: throw new NutritionException(resolution + " - " + RESOLUTION_IS_NOT_CORRECT_MSG);
        }

        return resolutionEnum;
    }

}