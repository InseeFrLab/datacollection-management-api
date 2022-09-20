package fr.insee.survey.datacollectionmanagement.questioning.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(indexes = {
    @Index(name = "mc_idPartitioning_index", columnList = "idPartitioning"), @Index(name = "mc_idSource_index", columnList = "idSource"),
    @Index(name = "mc_year_index", columnList = "year_value"), @Index(name = "mc_period_index", columnList = "period_value"),

})
public class MetadataCopy {

    @Id
    @GeneratedValue
    private Long id;
    private String idPartitioning;
    private String idSource;
    @Column(name="YEAR_VALUE")
    private Integer year;
    @Column(name="PERIOD_VALUE")
    private String period;

}
