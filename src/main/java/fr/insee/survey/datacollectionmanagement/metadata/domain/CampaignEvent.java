package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CampaignEvent {

    @Id
    @GeneratedValue
    private Long id;
    
    private String type;
    private Date date;

    @ManyToOne
    private Campaign campaign;
    
}
