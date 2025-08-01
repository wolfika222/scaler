package com.scaler.mapper;

import com.scaler.dto.PatientDTO;
import com.scaler.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface PatientEntityMapper {
    @Mapping(source = "address", target = "address", qualifiedByName = "toEmbeddable")
    PatientEntity toEntity(PatientDTO dto);

    @Mapping(source = "address", target = "address", qualifiedByName = "toFhirList")
    PatientDTO toDto(PatientEntity entity);
}
