package fr.insee.survey.datacollectionmanagement.metadata.dto;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignEventDto extends RepresentationModel<CampaignEventDto> {

    private Long id;
    private String type;
    private Date date;
}
