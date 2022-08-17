package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
    @Index(name = "campainId_index", columnList = "campaign_campaign_id")
  })
public class Partitioning {

    @Id
    private String id;
    
    private String status;   
    private Date openingDate;
    private Date closingDate;   
    private Date returnDate;
    
    
    @OneToOne
    @JsonManagedReference
    private Campaign campaign;


}
