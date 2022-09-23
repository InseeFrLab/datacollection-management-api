package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter 
@Table(indexes = {
    @Index(name = "surveyyear_index", columnList = "year_value"),
    @Index(name = "souce_index", columnList = "source_id_source")
  })
public class Survey {
    
    @Id
    private String id;
    @Column(name="YEAR_VALUE")
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
    @JsonBackReference
    private Set<Campaign> campaigns;
    
    @OneToOne
    @JsonManagedReference
    private Source source;
    
    

}
