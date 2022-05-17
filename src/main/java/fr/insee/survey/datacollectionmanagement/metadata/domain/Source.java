package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Source {
    
    @Id
    private String idSource;
    private String longWording;
    private String shortWording;
    private String periodicity;
    
    @OneToMany
    private Set<Survey> surveys;
    

}
