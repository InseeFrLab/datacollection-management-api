package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
    @Index(name = "companyName_index", columnList = "companyName")
  })
public class SurveyUnit {

    @Id
    private String idSu;

    @OneToMany
    private Set<Questioning> questionings;

    private String surveyUnitId;

    //"Raison Sociale"
    private String companyName;
}
