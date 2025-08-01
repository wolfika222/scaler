package com.scaler.service;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.scaler.dto.PatientDTO;
import com.scaler.entity.PatientEntity;
import com.scaler.mapper.PatientEntityMapper;
import com.scaler.mapper.PatientFhirMapper;
import com.scaler.repository.PatientRepository;
import com.scaler.service.impl.PatientServiceImpl;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    private PatientRepository patientRepository;
    private PatientFhirMapper patientFhirMapper;
    private PatientEntityMapper patientEntityMapper;
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);
        patientFhirMapper = mock(PatientFhirMapper.class);
        patientEntityMapper = mock(PatientEntityMapper.class);

        patientService = new PatientServiceImpl(patientRepository, patientFhirMapper, patientEntityMapper);
    }

    @Test
    void testFindById() {
        Long id = 1L;
        IdType idType = new IdType(id.toString());

        PatientEntity entity = new PatientEntity();
        entity.setId(id);
        entity.setFamily("Test");

        PatientDTO dto = new PatientDTO();
        dto.setFamily("Test");

        Patient fhirPatient = new Patient();
        fhirPatient.addName().setFamily("Test");

        when(patientRepository.findById(id)).thenReturn(Optional.of(entity));
        when(patientEntityMapper.toDto(entity)).thenReturn(dto);
        when(patientFhirMapper.toFhir(dto)).thenReturn(fhirPatient);

        Patient result = patientService.findById(idType);

        assertNotNull(result);
        assertEquals("Test", result.getNameFirstRep().getFamily());
    }

    @Test
    void testSearchByFamily_TooShort_ShouldThrowException() {
        StringParam shortParam = new StringParam("Al");

        InvalidRequestException thrown = assertThrows(InvalidRequestException.class, () -> {
            patientService.searchByFamily(shortParam);
        });

        assertEquals("The Family name must be at least 3 characters.", thrown.getMessage());
    }

    @Test
    void testSave() {
        Patient patient = new Patient();
        patient.addName().setFamily("SaveTest");

        PatientDTO dto = new PatientDTO();
        dto.setFamily("SaveTest");

        PatientEntity entity = new PatientEntity();
        entity.setFamily("SaveTest");

        PatientEntity savedEntity = new PatientEntity();
        savedEntity.setId(1L);
        savedEntity.setFamily("SaveTest");

        PatientDTO savedDto = new PatientDTO();
        savedDto.setFamily("SaveTest");

        Patient savedFhir = new Patient();
        savedFhir.setId("1");
        savedFhir.addName().setFamily("SaveTest");

        when(patientFhirMapper.toDto(patient)).thenReturn(dto);
        when(patientEntityMapper.toEntity(dto)).thenReturn(entity);
        when(patientRepository.save(entity)).thenReturn(savedEntity);
        when(patientEntityMapper.toDto(savedEntity)).thenReturn(savedDto);
        when(patientFhirMapper.toFhir(savedDto)).thenReturn(savedFhir);

        MethodOutcome outcome = patientService.save(patient);

        assertNotNull(outcome);
        assertEquals("SaveTest", ((Patient) outcome.getResource()).getNameFirstRep().getFamily());
        assertEquals("Patient/1", outcome.getId().toUnqualifiedVersionless().getValue());
    }
}
