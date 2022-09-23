package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
    @Index(name = "companyName_index", columnList = "companyName"),
    @Index(name = "surveyUnitId_index", columnList = "surveyUnitId")
  })
public class SurveyUnit {

    @Id
    private String idSu;

    @OneToMany
    @JsonIgnore
    private Set<Questioning> questionings;

    private String surveyUnitId;

    //"Raison Sociale"
    private String companyName;
}
