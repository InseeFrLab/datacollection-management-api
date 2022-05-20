package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
