package fr.insee.survey.datacollectionmanagement.metadata.dto;

import fr.insee.survey.datacollectionmanagement.metadata.util.PeriodEnum;
import lombok.Data;

@Data
public class CampaignDto {

    private String id;
    private String surveyId;
    private int year;
    private String campaignWording;
    private PeriodEnum period;
}
