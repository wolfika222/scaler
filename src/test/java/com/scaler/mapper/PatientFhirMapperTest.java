package com.scaler.mapper;

import com.scaler.dto.PatientDTO;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientFhirMapperTest {

    private final PatientFhirMapper mapper = new PatientFhirMapperImpl();

    @Test
    void testToFhir() {
        PatientDTO dto = new PatientDTO();
        dto.setGiven("John");
        dto.setFamily("Doe");
        dto.setGender("male");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));
        dto.setAddress(List.of(new Address().addLine("Test street")));
        dto.setPhone("123456");

        Patient patient = mapper.toFhir(dto);

        assertEquals("John", patient.getNameFirstRep().getGivenAsSingleString());
        assertEquals("Doe", patient.getNameFirstRep().getFamily());
        assertEquals("male", patient.getGender().toCode());
    }
}
