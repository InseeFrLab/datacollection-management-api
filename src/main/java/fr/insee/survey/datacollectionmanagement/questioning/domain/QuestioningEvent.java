package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class QuestioningEvent {
    
    public enum QuestioningEventEnum {
        REFUSAL, VALINT, VALPAP, HC, PARTIELINT, WASTE, PND, FOLLOWUP, INITLA    
    }

    @Id
    @GeneratedValue
    private Long id;

    private Date date;
    private String type;

    @ManyToOne
    private Questioning questioning;
}
