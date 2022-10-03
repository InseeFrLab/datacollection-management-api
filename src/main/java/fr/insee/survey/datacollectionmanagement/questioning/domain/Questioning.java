package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
    @Index(name = "idPartitioning_index", columnList = "idPartitioning")
})
public class Questioning {

    @Id
    @GeneratedValue
    private Long id;

    private String modelName;
    private String idPartitioning;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<QuestioningAccreditation> questioningAccreditations;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SurveyUnit surveyUnit;

}
