package com.scaler.provider;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.scaler.mapper.PatientFhirMapper;
import com.scaler.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PatientProvider implements IResourceProvider {
    private final PatientService patientService;
    private final PatientFhirMapper patientFhirMapper;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Patient.class;
    }

    @Read()
    public Patient read(@IdParam IdType id) {
        return patientService.findById(id);
    }

    @Create()
    public MethodOutcome create(@ResourceParam Patient patient) {
        return patientService.save(patient);
    }

    @Search()
    @PreAuthorize("hasRole('READ')")
    public List<Patient> findAll() {
        return patientService.findAll();
    }

    @Search()
    public List<Patient> searchByFamily(@RequiredParam(name = Patient.SP_FAMILY) StringParam family) {
        return patientService.searchByFamily(family);
    }
}