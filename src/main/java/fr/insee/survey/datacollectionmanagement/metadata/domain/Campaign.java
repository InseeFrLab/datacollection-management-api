package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Campaign {
    
    @Id
    private String campaignId;
    
    private String year;    
    private String campaignWording;
    private String period;
    
    @OneToMany
    private Set<Partitioning> partitionings;
    
    @OneToMany
    private Set<CampaignEvent> capmpaignEvents;
    

}
