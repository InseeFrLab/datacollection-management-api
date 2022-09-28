package fr.insee.survey.datacollectionmanagement.contact.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactEventDto {

    private Long id;
    private String identifier;
    private Date eventDate;
    private String type;
    private JsonNode payload;

}
