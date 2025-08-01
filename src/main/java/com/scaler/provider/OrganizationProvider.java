package com.scaler.provider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.scaler.dto.OrganizationDTO;
import com.scaler.mapper.OrganizationFhirMapper;
import com.scaler.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrganizationProvider implements IResourceProvider {

    private final OrganizationService organizationService;
    private final OrganizationFhirMapper organizationFhirMapper;

    @Override
    public Class<Organization> getResourceType() {
        return Organization.class;
    }

    @Create
    public MethodOutcome createOrganization(Organization organization) {
        OrganizationDTO dto = organizationFhirMapper.toDto(organization);
        OrganizationDTO saved = organizationService.save(dto);
        Organization savedFhir = organizationFhirMapper.toFhir(saved);
        return new MethodOutcome(new IdType("Organization", saved.getId().toString()))
                .setResource(savedFhir);
    }

    @Read()
    public Organization getOrganizationById(IdType id) {
        Long orgId = Long.parseLong(id.getIdPart());
        return organizationFhirMapper.toFhir(
                organizationService.findAll()
                        .stream()
                        .filter(o -> o.getId().equals(orgId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Organization not found"))
        );
    }

    @Search()
    public List<Organization> getAllOrganizations() {
        return organizationService.findAll()
                .stream()
                .map(organizationFhirMapper::toFhir)
                .collect(Collectors.toList());
    }
}
