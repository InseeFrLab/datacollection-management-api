package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SurveyUnit {

    @Id
    private String idSu;

    @OneToMany
    private Set<Questioning> questionings;

    private String surveyUnitId;

    //"Raison Sociale"
    private String companyName;
}
