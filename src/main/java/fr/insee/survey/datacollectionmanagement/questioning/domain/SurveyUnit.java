package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @Index(name = "identificationName_index", columnList = "identificationName"), @Index(name = "identificationCode_index", columnList = "identificationCode")
})
public class SurveyUnit {

    @Id
    private String idSu;

    @OneToMany
    private Set<Questioning> questionings;

    private String identificationCode;

    // "Raison Sociale"
    private String identificationName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SurveyUnitAddress surveyUnitAddress;

}
