package fr.insee.survey.datacollectionmanagement.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchContactDto {

    private String identifier;
    private String firstName;
    private String lastName;
    private String email;
}
