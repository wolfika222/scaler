package com.scaler.service;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface PatientService {
//    @PreAuthorize("hasRole('ROLE_READ')")
    Patient findById(@IdParam IdType id);

//    @PreAuthorize("hasRole('ROLE_WRITE')")
    MethodOutcome save(Patient patient);

    List<Patient> findAll();

//    @PreAuthorize("hasRole('ROLE_READ')")
    List<Patient> searchByFamily(StringParam family);
}
