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
    private String repetitionIndex;
    private String streetType;
    private String streetName;
    private String addressSupplement;
    private String cityName;
    private String zipCode;
    private String cedexCode;
    private String cedexName;
    private String specialDistribution;
    private String countryCode;
    private String countryName;

}
