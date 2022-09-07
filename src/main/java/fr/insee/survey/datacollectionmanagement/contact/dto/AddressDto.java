package fr.insee.survey.datacollectionmanagement.contact.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto extends RepresentationModel<AddressDto> {

    private Long id;
    private String streetNumber;
    private String streetName;
    private String city;
    private String zipCode;
    private String countryName;

}
