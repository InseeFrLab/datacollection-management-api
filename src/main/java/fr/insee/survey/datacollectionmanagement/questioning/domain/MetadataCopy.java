package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(indexes = {
    @Index(name = "mc_idPartitioning_index", columnList = "idPartitioning"), @Index(name = "mc_idSource_index", columnList = "idSource"),
    @Index(name = "mc_year_index", columnList = "year"), @Index(name = "mc_period_index", columnList = "period"),

})
public class MetadataCopy {

    @Id
    @GeneratedValue
    private Long id;
    private String idPartitioning;
    private String idSource;
    private Integer year;
    private String period;

}
