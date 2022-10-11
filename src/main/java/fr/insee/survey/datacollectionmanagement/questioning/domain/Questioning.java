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

import lombok.Data;

@Entity
@Data
@Table(indexes = {
        @Index(name = "idPartitioning_index", columnList = "idPartitioning")
})
public class Questioning {

    @Id
    @GeneratedValue
    private Long id;

    private String modelName;
    private String idPartitioning;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<QuestioningAccreditation> questioningAccreditations;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<QuestioningEvent> questioningEvents;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SurveyUnit surveyUnit;

}
