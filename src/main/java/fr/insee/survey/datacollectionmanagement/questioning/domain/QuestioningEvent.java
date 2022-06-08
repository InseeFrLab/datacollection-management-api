package fr.insee.survey.datacollectionmanagement.questioning.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
