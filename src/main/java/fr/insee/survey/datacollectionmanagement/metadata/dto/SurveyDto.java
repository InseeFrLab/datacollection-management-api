package fr.insee.survey.datacollectionmanagement.metadata.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class SurveyDto {

    private String id;
    @NonNull
    private String sourceId;
    private Integer year;
    private Integer sampleSize;
    private String longWording;
    private String shortWording;
    private String shortObjectives;
    private String longObjectives;
    private String visaNumber;
    private String cnisUrl;
    private String diffusionUrl;
    private String noticeUrl;
    private String specimenUrl;
    private String communication;
}
