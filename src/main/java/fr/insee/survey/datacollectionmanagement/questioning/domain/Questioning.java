package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Questioning {

    @Id
    @GeneratedValue
    private Long id;

    private String modelName;
    private String idPartitioning;


    @OneToMany
    private Set<QuestioningAccreditation> questioningAccreditations;
    
    @OneToOne
    private SurveyUnit surveyUnit;


}
