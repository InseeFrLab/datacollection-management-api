package fr.insee.survey.datacollectionmanagement.metadata.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportDto {

    private String id;
    private String label;
    private String phoneNumber;
    private String mail;
    private String countryName;
    private String streetNumber;
    private String streetName;
    private String city;
    private String zipCode;

}
