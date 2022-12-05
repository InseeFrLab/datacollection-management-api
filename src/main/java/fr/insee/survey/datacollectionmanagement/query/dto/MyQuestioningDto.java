package fr.insee.survey.datacollectionmanagement.query.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyQuestioningDto {

    private String identificationCode;
    private String surveyWording;
    private String surveyObjectives;
    private String accessUrl;
    private boolean mandatoryMySurveys;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp openingDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp closingDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp returnDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp questioningDate;
    private String questioningStatus;

}
