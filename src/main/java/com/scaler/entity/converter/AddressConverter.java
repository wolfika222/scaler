package com.scaler.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Address;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = false)
public class AddressConverter implements AttributeConverter<Address, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Address attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize Address", e);
        }
    }

    @Override
    public Address convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, Address.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not deserialize Address", e);
        }
    }
}