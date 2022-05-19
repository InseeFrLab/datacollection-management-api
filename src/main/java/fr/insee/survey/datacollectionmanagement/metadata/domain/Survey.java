package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Survey {
    
    @Id
    private String id;
    private Integer year;
    private boolean isMandatory;
    private Integer sampleSize;
    private String longWording;
    private String shortWording;
    private String shortObjectives;
    private String longObjectives;
    private String visaNumber;
    private String cnisUrl;
    private String diffusionUrl;
    private String noticeUrl;
    private String specimenUrl;
    private String communication;
    
    @OneToMany
    private Set<Campaign> campaigns;
    
    @OneToOne
    private Source source;
    
    

}
