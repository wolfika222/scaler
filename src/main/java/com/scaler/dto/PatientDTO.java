package com.scaler.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.r4.model.Address;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
public class PatientDTO {
    private String id;
    private String family;
    private String given;
    private String gender;
    private LocalDate birthDate;
    private List<Address> address;
    private String phone;
}