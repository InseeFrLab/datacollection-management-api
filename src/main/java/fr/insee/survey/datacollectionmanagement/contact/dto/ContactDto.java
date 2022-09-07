package fr.insee.survey.datacollectionmanagement.contact.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto extends RepresentationModel<ContactDto> {

    private String identifier;
    private String civility;
    private String lastName;
    private String firstName;
    private String function;
    private String email;
    private String phone;

}
