package ru.darin.nutrition_recommendation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.darin.nutrition_recommendation.dto.ProtocolDTO;
import ru.darin.nutrition_recommendation.model.Protocol;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProtocolMapper {

    @Mapping(source = "protocolTitle",target = "protocolTitle")
    @Mapping(source = "protocolId", target = "protocol_id")
    Protocol toProtocol(ProtocolDTO protocolDTO);

    @Mapping(source = "protocolTitle",target = "protocolTitle")
    @Mapping(source = "protocol_id", target = "protocolId")
    ProtocolDTO toProtocolDTO(Protocol protocol);

}