package com.scaler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressEmbeddable {

    private String line;
    private String city;
    private String postalCode;
    private String country;
}