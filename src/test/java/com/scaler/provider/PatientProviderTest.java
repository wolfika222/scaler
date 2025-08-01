package com.scaler.provider;

import ca.uhn.fhir.rest.param.StringParam;
import com.scaler.mapper.PatientFhirMapper;
import com.scaler.service.PatientService;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PatientProviderTest {

    @Test
    void testSearchByFamily() {
        PatientService patientService = mock(PatientService.class);
        PatientFhirMapper mapper = mock(PatientFhirMapper.class);
        PatientProvider provider = new PatientProvider(patientService, mapper);

        Patient patient = new Patient();
        patient.setId("123");
        patient.setGender(Enumerations.AdministrativeGender.MALE);

        when(patientService.searchByFamily(any(StringParam.class)))
                .thenReturn(List.of(patient));

        List<Patient> result = provider.searchByFamily(new StringParam("Smith"));

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getIdElement().getIdPart());
    }
}
