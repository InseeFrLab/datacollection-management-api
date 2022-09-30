package fr.insee.survey.datacollectionmanagement.questioning.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SurveyUnitAddress {

    @Id
    @GeneratedValue
    private Long id;

    private String countryName;
    private String streetNumber;
    private String streetName;
    private String city;
    private String zipCode;
}
