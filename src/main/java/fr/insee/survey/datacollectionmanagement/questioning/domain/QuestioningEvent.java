package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

@Entity
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class QuestioningEvent {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;
    @Enumerated(EnumType.STRING)
    private TypeQuestioningEvent type;

    @OneToOne
    private Questioning questioning;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    public QuestioningEvent(Date date, TypeQuestioningEvent type, Questioning questioning) {
        this.date = date;
        this.type = type;
        this.questioning = questioning;
    }

    public QuestioningEvent() {}
}
