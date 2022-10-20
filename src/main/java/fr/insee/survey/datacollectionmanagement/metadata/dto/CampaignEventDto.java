package fr.insee.survey.datacollectionmanagement.metadata.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CampaignEventDto {

    private Long id;
    private String type;
    private Date date;
}
