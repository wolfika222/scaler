package com.scaler.mapper;

import com.scaler.dto.OrganizationDTO;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationFhirMapper {

    public OrganizationDTO toDto(Organization fhirOrg) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setId(fhirOrg.hasIdElement() ? Long.parseLong(fhirOrg.getIdElement().getIdPart()) : null);
        dto.setName(fhirOrg.hasName() ? fhirOrg.getName() : null);

        if (!fhirOrg.getAddress().isEmpty()) {
            Address address = fhirOrg.getAddressFirstRep();
            dto.setAddress(address.getText());
        }

        if (!fhirOrg.getTelecom().isEmpty()) {
            ContactPoint phone = fhirOrg.getTelecomFirstRep();
            dto.setPhone(phone.getValue());
        }

        return dto;
    }

    public Organization toFhir(OrganizationDTO dto) {
        Organization fhirOrg = new Organization();

        if (dto.getId() != null) {
            fhirOrg.setId(dto.getId().toString());
        }

        fhirOrg.setName(dto.getName());

        if (dto.getAddress() != null) {
            Address address = new Address();
            address.setText(dto.getAddress());
            fhirOrg.addAddress(address);
        }

        if (dto.getPhone() != null) {
            ContactPoint telecom = new ContactPoint();
            telecom.setSystem(ContactPoint.ContactPointSystem.PHONE);
            telecom.setValue(dto.getPhone());
            fhirOrg.addTelecom(telecom);
        }

        return fhirOrg;
    }
}
