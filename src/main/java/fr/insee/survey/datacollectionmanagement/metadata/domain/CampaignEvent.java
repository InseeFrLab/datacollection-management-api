package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class CampaignEvent {

    @Id
    @GeneratedValue
    private Long id;
    
    private String type;
    private Date date;
    
}
