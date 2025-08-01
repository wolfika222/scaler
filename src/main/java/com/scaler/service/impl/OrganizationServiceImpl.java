package com.scaler.service.impl;

import com.scaler.dto.OrganizationDTO;
import com.scaler.entity.OrganizationEntity;
import com.scaler.mapper.OrganizationMapper;
import com.scaler.repository.OrganizationRepository;
import com.scaler.service.OrganizationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public OrganizationDTO save(OrganizationDTO organizationDTO) {
        log.debug("save() called with OrganizationDTO: {}", organizationDTO);

        OrganizationEntity entity = organizationMapper.toEntity(organizationDTO);
        log.debug("Mapped to OrganizationEntity: {}", entity);

        OrganizationEntity saved = organizationRepository.save(entity);
        log.debug("Saved OrganizationEntity: {}", saved);

        OrganizationDTO result = organizationMapper.toDto(saved);
        log.debug("Mapped back to DTO: {}", result);

        return result;
    }

    @Override
    public List<OrganizationDTO> findAll() {
        log.debug("findAll() called");

        List<OrganizationEntity> entities = organizationRepository.findAll();
        log.debug("Found {} organizations", entities.size());

        List<OrganizationDTO> dtoList = entities.stream()
                .map(organizationMapper::toDto)
                .collect(Collectors.toList());

        log.debug("Mapped to DTO list: {}", dtoList);
        return dtoList;
    }
}

