package fr.insee.survey.datacollectionmanagement.metadata.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignDto extends RepresentationModel<CampaignDto>{

    private String campaignId;
    private int year;
    private String campaignWording;
    private String period;
}
