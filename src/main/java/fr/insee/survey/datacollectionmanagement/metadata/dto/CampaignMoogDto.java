package fr.insee.survey.datacollectionmanagement.metadata.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CampaignMoogDto implements Serializable {

    private static final long serialVersionUID = 6628857938862106451L;
    private String id;
    private String label;
    private Long collectionStartDate;
    private Long collectionEndDate;

}
