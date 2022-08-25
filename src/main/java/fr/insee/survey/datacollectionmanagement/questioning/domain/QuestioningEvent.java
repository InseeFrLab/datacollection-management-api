package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fr.insee.survey.datacollectionmanagement.metadata.util.TypeQuestioningEvent;
import lombok.Data;

@Entity
@Data
public class QuestioningEvent {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;
    private String type;

    @ManyToOne
    private Questioning questioning;

    public QuestioningEvent(Date date, String type, Questioning questioning) {
        this.date = date;
        this.type = type;
        this.questioning = questioning;
    }

    public QuestioningEvent() {
    }
}
