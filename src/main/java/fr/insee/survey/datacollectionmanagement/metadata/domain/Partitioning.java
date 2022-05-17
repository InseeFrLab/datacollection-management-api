package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
    
    
}
