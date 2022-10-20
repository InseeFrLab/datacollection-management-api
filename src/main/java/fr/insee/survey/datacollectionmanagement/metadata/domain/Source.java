package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Source {

    @Id
    private String id;
    private String longWording;
    private String shortWording;
    private String periodicity;
    private boolean mandatoryMySurveys;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Survey> surveys;

    @OneToOne
    private Owner owner;
    
    @OneToOne
    private Support support;

}
