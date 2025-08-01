package com.scaler.mapper;

import com.scaler.entity.AddressEmbeddable;
import org.hl7.fhir.r4.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Named("toEmbeddable")
    default AddressEmbeddable toEmbeddable(List<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return null;
        }
        Address fhirAddress = addresses.get(0);
        return new AddressEmbeddable(
                fhirAddress.getLine().isEmpty() ? null : fhirAddress.getLine().get(0).getValue(),
                fhirAddress.getCity(),
                fhirAddress.getPostalCode(),
                fhirAddress.getCountry()
        );
    }

    @Named("toFhirList")
    default List<Address> toFhirList(AddressEmbeddable embeddable) {
        if (embeddable == null) return null;

        Address fhirAddress = new Address();
        if (embeddable.getLine() != null) {
            fhirAddress.addLine(embeddable.getLine());
        }
        fhirAddress.setCity(embeddable.getCity());
        fhirAddress.setPostalCode(embeddable.getPostalCode());
        fhirAddress.setCountry(embeddable.getCountry());
        return List.of(fhirAddress);
    }
}
