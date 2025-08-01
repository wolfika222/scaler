package com.scaler.entity;

import com.scaler.entity.converter.AddressConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hl7.fhir.r4.model.Address;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patients")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String family;
    private String given;
    private String gender;
    private LocalDate birthDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "line", column = @Column(name = "address_line")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "address_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "address_country"))
    })
    private AddressEmbeddable address;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "managing_org_id", referencedColumnName = "id")
    private OrganizationEntity managingOrganization;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
