package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
    @Index(name = "year_index", columnList = "year"),
    @Index(name = "surveyid_index", columnList = "survey_id")
  })
public class Campaign {

    @Id
    private String campaignId;

    private int year;
    private String campaignWording;
    private String period;

    @OneToMany
    private Set<Partitioning> partitionings;

    @OneToOne
    private Survey survey;

}
