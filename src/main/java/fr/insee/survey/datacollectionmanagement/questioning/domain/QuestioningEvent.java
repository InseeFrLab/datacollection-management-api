package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import fr.insee.survey.datacollectionmanagement.questioning.util.TypeQuestioningEvent;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_upload")
    @JsonManagedReference
    private Upload upload;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    public QuestioningEvent(Date date, TypeQuestioningEvent type, Questioning questioning) {
        this.date = date;
        this.type = type;
        this.questioning = questioning;
    }

    public QuestioningEvent() {
    }

    @Override
    public String toString() {
        return "QuestioningEvent [id=" + id + ", date=" + date + ", type=" + type + ", payload=" + payload
                + "]";
    }

}
