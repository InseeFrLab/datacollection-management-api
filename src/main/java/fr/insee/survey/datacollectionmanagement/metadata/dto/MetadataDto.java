package fr.insee.survey.datacollectionmanagement.metadata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MetadataDto {

    @JsonProperty("partitioning")
    private PartitioningDto partitioningDto;
    @JsonProperty("campaign")
    private CampaignDto campaignDto;
    @JsonProperty("survey")
    private SurveyDto surveyDto;
    @JsonProperty("source")
    private SourceDto sourceDto;
    @JsonProperty("owner")
    private OwnerDto ownerDto;
    @JsonProperty("support")
    private SupportDto supportDto;

}
