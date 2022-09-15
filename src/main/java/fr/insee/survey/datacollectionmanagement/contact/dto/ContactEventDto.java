package fr.insee.survey.datacollectionmanagement.contact.dto;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class ContactEventDto extends RepresentationModel<ContactEventDto> {

    private Long id;
    private String identifier;
    private Date eventDate;
    private String type;
    private JSONObject payload;

}
