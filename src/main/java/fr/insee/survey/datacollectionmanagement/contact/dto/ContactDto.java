package fr.insee.survey.datacollectionmanagement.contact.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto{

    private String identifier;
    private String civility;
    private String lastName;
    private String firstName;
    private String function;
    private String email;
    private String phone;
    private AddressDto address;

}
