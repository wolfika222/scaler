package com.scaler.mapper;

import com.scaler.dto.OrganizationDTO;
import com.scaler.entity.OrganizationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    OrganizationDTO toDto(OrganizationEntity entity);

    OrganizationEntity toEntity(OrganizationDTO dto);
}
