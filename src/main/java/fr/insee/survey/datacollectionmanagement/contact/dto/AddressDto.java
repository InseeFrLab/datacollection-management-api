package fr.insee.survey.datacollectionmanagement.contact.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto  {

    @JsonIgnore
    private Long id;
    private String streetNumber;
    private String streetName;
    private String city;
    private String zipCode;
    private String countryName;

}
