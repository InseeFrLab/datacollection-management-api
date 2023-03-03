package fr.insee.survey.datacollectionmanagement.user.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserEventDto {

    private Long id;
    private String identifier;
    private Date eventDate;
    private String type;
    private JsonNode payload;

}
