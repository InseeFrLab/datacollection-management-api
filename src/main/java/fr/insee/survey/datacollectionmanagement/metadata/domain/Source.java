package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Source {
    
    @Id
    private String idSource;
    private String longWording;
    private String shortWording;
    private String periodicity;
    
    @OneToMany
    @JsonBackReference
    private Set<Survey> surveys;
    

}
