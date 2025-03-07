package ru.darin.nutrition_recommendation.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.model.Person;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PersonMapper {

    @Mapping(source = "fullName", target = "fullName")
    Person toPerson(PersonDTO dto);

    @Mapping(source = "fullName", target = "fullName")
    PersonDTO toPersonDto(Person person);

}
