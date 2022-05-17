package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Support {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String label;
    
     @OneToMany
     private Set<Source> sources;
     

}
