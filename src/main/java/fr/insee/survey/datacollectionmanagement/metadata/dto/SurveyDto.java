package fr.insee.survey.datacollectionmanagement.metadata.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyDto extends RepresentationModel<SurveyDto> {

    private String id;
    private Integer year;
    private boolean isMandatory;
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
