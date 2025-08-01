package com.scaler.service.impl;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.scaler.dto.PatientDTO;
import com.scaler.entity.PatientEntity;
import com.scaler.mapper.PatientEntityMapper;
import com.scaler.mapper.PatientFhirMapper;
import com.scaler.repository.PatientRepository;
import com.scaler.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientFhirMapper patientFhirMapper;
    private final PatientEntityMapper patientEntityMapper;

    @Override
    public Patient findById(@IdParam IdType id) {
        log.debug("findById() called with ID: {}", id.getIdPart());
        PatientEntity patientEntity = patientRepository.findById(Long.parseLong(id.getIdPart()))
                .orElseThrow(() -> {
                    log.warn("Patient not found for ID: {}", id.getIdPart());
                    return new ResourceNotFoundException("Patient not found");
                });
        log.debug("PatientEntity found: {}", patientEntity);

        PatientDTO dto = patientEntityMapper.toDto(patientEntity);
        Patient fhirPatient = patientFhirMapper.toFhir(dto);
        fhirPatient.setId(id.getIdPart());

        log.debug("Returning FHIR Patient: {}", fhirPatient);
        return fhirPatient;
    }

    @Override
    @Create
    public MethodOutcome save(Patient patient) {
        log.debug("save() called with Patient: {}", patient);

        PatientDTO dto = patientFhirMapper.toDto(patient);
        log.debug("Mapped PatientDTO: {}", dto);

        PatientEntity entity = patientEntityMapper.toEntity(dto);
        log.debug("Mapped PatientEntity: {}", entity);

        PatientEntity saved = patientRepository.save(entity);
        log.debug("Saved PatientEntity: {}", saved);

        Patient createdPatient = patientFhirMapper.toFhir(patientEntityMapper.toDto(saved));

        MethodOutcome outcome = new MethodOutcome();
        outcome.setId(new IdType("Patient", saved.getId()));
        outcome.setResource(createdPatient);

        log.debug("Returning MethodOutcome with ID: {}", outcome.getId());
        return outcome;
    }

    @Override
    @Search()
    public List<Patient> findAll() {
        log.debug("findAll() called");
        List<PatientEntity> entities = patientRepository.findAll();
        log.debug("Found {} patients", entities.size());

        return entities.stream()
                .map(entity -> {
                    PatientDTO dto = patientEntityMapper.toDto(entity);
                    return patientFhirMapper.toFhir(dto);
                })
                .toList();
    }

    @Override
    @Search()
    public List<Patient> searchByFamily(@RequiredParam(name = Patient.SP_FAMILY) StringParam family) {
        log.debug("searchByFamily() called with family: {}", family.getValue());

        if (family.getValue().length() < 3) {
            log.warn("Family name too short: {}", family.getValue());
            throw new InvalidRequestException("The Family name must be at least 3 characters.");
        }

        List<PatientEntity> entities = patientRepository.findByFamilyContainingIgnoreCase(family.getValue());
        log.debug("Found {} patients with family name like '{}'", entities.size(), family.getValue());

        return entities.stream()
                .map(entity -> {
                    PatientDTO dto = patientEntityMapper.toDto(entity);
                    return patientFhirMapper.toFhir(dto);
                })
                .toList();
    }
}
