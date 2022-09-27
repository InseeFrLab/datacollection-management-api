package fr.insee.survey.datacollectionmanagement.contact.dto;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactEventDto extends RepresentationModel<ContactEventDto> {

    private Long id;
    private String identifier;
    private Date eventDate;
    private String type;
    private JsonNode payload;

}
