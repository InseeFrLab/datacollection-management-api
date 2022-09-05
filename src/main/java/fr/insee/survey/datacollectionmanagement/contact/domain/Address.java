package fr.insee.survey.datacollectionmanagement.contact.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {

    @Id @GeneratedValue
    private Long id;

    private String countryName;
    private String streetNumber;
    private String streetName;
    private String city;
    private String zipCode;
}
