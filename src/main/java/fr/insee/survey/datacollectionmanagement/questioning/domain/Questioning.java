package fr.insee.survey.datacollectionmanagement.questioning.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
public class Questioning {

    @Id
    @GeneratedValue
    private Long id;

    private String modelName;


    @OneToMany
    private Set<QuestioningAccreditation> questioningAccreditations;


}
