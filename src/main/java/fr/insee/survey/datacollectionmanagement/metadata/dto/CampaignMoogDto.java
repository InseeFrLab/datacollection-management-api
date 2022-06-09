package fr.insee.survey.datacollectionmanagement.metadata.dto;

import java.io.Serializable;

public class CampaignMoogDto implements Serializable {

    private String id;
    private String label;
    private Long collectionStartDate;
    private Long collectionEndDate;

    public CampaignMoogDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getCollectionStartDate() {
        return collectionStartDate;
    }

    public void setCollectionStartDate(Long collectionStartDate) {
        this.collectionStartDate = collectionStartDate;
    }

    public Long getCollectionEndDate() {
        return collectionEndDate;
    }

    public void setCollectionEndDate(Long collectionEndDate) {
        this.collectionEndDate = collectionEndDate;
    }
}
