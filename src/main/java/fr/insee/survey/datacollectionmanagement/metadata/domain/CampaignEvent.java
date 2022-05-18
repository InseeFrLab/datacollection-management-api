package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class CampaignEvent {

    @Id
    @GeneratedValue
    private Long id;
    
    private String type;
    private Date date;

    @ManyToOne
    private Campaign campaign;
    
}
