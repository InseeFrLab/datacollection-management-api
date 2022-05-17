package fr.insee.survey.datacollectionmanagement.questioning.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class QuestioningEvent {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;
    private String type;

    @OneToMany
    private Set<Questioning> questionings;
}
