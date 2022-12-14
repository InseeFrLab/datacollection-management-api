package fr.insee.survey.datacollectionmanagement.contact.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactFirstLoginDto{

    private String identifier;
    private String externalId;
    private String civility;
    private String lastName;
    private String firstName;
    private String function;
    private String email;
    private String phone;
    private boolean firstConnect;
    private AddressDto address;

}
