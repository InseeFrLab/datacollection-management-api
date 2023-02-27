package fr.insee.survey.datacollectionmanagement.user.dto;


import fr.insee.survey.datacollectionmanagement.contact.dto.AddressDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto{

    private String identifier;
    private String role;

}

