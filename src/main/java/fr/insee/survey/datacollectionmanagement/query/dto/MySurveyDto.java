package fr.insee.survey.datacollectionmanagement.query.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MySurveyDto {
    
    private String surveyUnitId;
    private String surveyWording;
    private String surveyObjectives;
    private Timestamp monitoringDate;
    private String monitoringStatus;
    private String accessUrl;
    private Timestamp questioningDate;
    private String questioningStatus;


}
