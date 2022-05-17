package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import lombok.Data;

@Entity
@Data
public class Partitioning {

    @Id
    private String id;
    
    private String Status;   
    private Date openingDate;
    private Date closingDate;   
    private Date returnDate;
    
    
    @OneToOne
    private Campaign campaign;

    @OneToMany
    private Set<Partitioning> partitionings;

}
