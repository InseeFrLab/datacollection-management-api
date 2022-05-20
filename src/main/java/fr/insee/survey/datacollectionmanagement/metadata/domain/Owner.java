package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Owner {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String label;
    
    @OneToMany
    private Set<Source> sources;

}