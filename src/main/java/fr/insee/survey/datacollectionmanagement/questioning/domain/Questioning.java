package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @OneToMany
    @JsonManagedReference
    private Set<QuestioningAccreditation> questioningAccreditations;

    @OneToOne
    @JsonBackReference
    private SurveyUnit surveyUnit;

}
