package ru.darin.nutrition_recommendation.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.darin.nutrition_recommendation.dto.PersonDTO;
import ru.darin.nutrition_recommendation.model.Person;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = ProtocolMapper.class)
public interface PersonMapper {

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(target = "protocols", source = "protocols")
    Person toPerson(PersonDTO dto);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(target = "protocols", source = "protocols")
    @Mapping(target = "personId", source = "personId")
    PersonDTO toPersonDto(Person person);

}