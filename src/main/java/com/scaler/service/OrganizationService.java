package com.scaler.service;

import com.scaler.dto.OrganizationDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface OrganizationService {
    @PreAuthorize("hasRole('ROLE_WRITE')")
    OrganizationDTO save(OrganizationDTO organizationDTO);

    @PreAuthorize("hasRole('ROLE_READ')")
    List<OrganizationDTO> findAll();
}
