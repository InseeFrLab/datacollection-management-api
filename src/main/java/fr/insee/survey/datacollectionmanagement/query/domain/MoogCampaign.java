package fr.insee.survey.datacollectionmanagement.query.domain;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MoogCampaign {

    private String id;
    private String label;
    private Long collectionStartDate;
    private Long collectionEndDate;

}
