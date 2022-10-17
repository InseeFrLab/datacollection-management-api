package fr.insee.survey.datacollectionmanagement.metadata.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignDto {

    private String id;
    private String surveyId;
    private int year;
    private String campaignWording;
    private String period;
}
