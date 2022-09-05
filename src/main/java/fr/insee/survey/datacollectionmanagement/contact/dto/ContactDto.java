package fr.insee.survey.datacollectionmanagement.contact.dto;

import java.util.Set;

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact.Gender;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {
    
    private String identifier;
    private String campaignId;

    private String lastName;
    private String firstName;
    private String email;
    private String function;
    private String phone;
    private String comment;
    private Address address;
    private Set<ContactEvent> contactEvents;
    private Gender gender;

}
