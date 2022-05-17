package fr.insee.survey.datacollectionmanagement.contact.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Address {

    @Id @GeneratedValue
    private Long id;

    private String countryName;
    private long streetNumber;
    private String streetName;
}
