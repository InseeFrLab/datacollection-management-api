package fr.insee.survey.datacollectionmanagement.metadata.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PartitioningDto {

    private String id;
    private String campaignId;
    private String status;
    private String label;
    private Date openingDate;
    private Date closingDate;
    private Date returnDate;
}
