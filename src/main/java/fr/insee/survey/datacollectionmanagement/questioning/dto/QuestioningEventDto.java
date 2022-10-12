package fr.insee.survey.datacollectionmanagement.questioning.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestioningEventDto {

    private Long id;
    private String questioningId;
    private Date eventDate;
    private String type;
    private JsonNode payload;

}
