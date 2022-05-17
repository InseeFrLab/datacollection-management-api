package fr.insee.survey.datacollectionmanagement.questioning.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class QuestioningEvent {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;
    private String type;
}
