package com.scaler.mapper;

import com.scaler.dto.PatientDTO;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.AdministrativeGender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientFhirMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = ".", qualifiedByName = "mapName")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "mapGender")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "telecom", source = "phone", qualifiedByName = "mapPhone")
    Patient toFhir(PatientDTO dto);

    @Mapping(target = "id", expression = "java(patient.getIdElement().getIdPart())")
    @Mapping(target = "given", expression = "java(getGivenName(patient))")
    @Mapping(target = "family", expression = "java(getFamilyName(patient))")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "mapGenderReverse")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "address", expression = "java(patient.getAddress())")
    @Mapping(target = "phone", expression = "java(getPhone(patient))")
    PatientDTO toDto(Patient patient);

    // -------------------------
    // MAPPING HELPERS
    // -------------------------

    @Named("mapGender")
    default Enumerations.AdministrativeGender mapGender(String gender) {
        if (gender == null) return Enumerations.AdministrativeGender.UNKNOWN;
        return switch (gender.toLowerCase()) {
            case "male" -> Enumerations.AdministrativeGender.MALE;
            case "female" -> Enumerations.AdministrativeGender.FEMALE;
            case "other" -> Enumerations.AdministrativeGender.OTHER;
            default -> Enumerations.AdministrativeGender.UNKNOWN;
        };
    }

    @Named("mapGenderReverse")
    default String mapGenderReverse(Enumerations.AdministrativeGender gender) {
        if (gender == null) return null;
        return switch (gender) {
            case MALE -> "male";
            case FEMALE -> "female";
            case OTHER -> "other";
            default -> "unknown";
        };
    }

    @Named("mapName")
    default List<HumanName> mapName(PatientDTO dto) {
        return Collections.singletonList(
                new HumanName().setFamily(dto.getFamily()).addGiven(dto.getGiven())
        );
    }

    @Named("mapAddressList")
    default List<Address> mapAddressList(List<Address> addressList) {
        return addressList != null ? addressList : Collections.emptyList();
    }

    @Named("mapPhone")
    default List<ContactPoint> mapPhone(String phone) {
        if (phone == null) return Collections.emptyList();
        return Collections.singletonList(
                new ContactPoint().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(phone)
        );
    }

    // -------------------------
    // READ HELPERS
    // -------------------------

    default String getGivenName(Patient patient) {
        return patient.hasName() && !patient.getName().isEmpty()
                ? patient.getName().get(0).getGivenAsSingleString()
                : null;
    }

    default String getFamilyName(Patient patient) {
        return patient.hasName() && !patient.getName().isEmpty()
                ? patient.getName().get(0).getFamily()
                : null;
    }

    default String getPhone(Patient patient) {
        if (patient.hasTelecom()) {
            return patient.getTelecom().stream()
                    .filter(t -> t.getSystem() == ContactPoint.ContactPointSystem.PHONE)
                    .map(ContactPoint::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
