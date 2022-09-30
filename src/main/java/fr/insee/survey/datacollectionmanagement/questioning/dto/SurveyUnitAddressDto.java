package fr.insee.survey.datacollectionmanagement.questioning.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyUnitAddressDto  {

    @JsonIgnore
    private Long id;
    private String streetNumber;
    private String streetName;
    private String city;
    private String zipCode;
    private String countryName;

}
