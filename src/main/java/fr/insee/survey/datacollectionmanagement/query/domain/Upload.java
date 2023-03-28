package fr.insee.survey.datacollectionmanagement.query.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "uploads")
@Getter
@Setter
public class Upload {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_upload")
    @SequenceGenerator(name = "gen_upload", allocationSize = 1, sequenceName = "seq_upload")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "dateupload")
    private Long date;

    @JsonBackReference
    @OneToMany(mappedBy = "upload")
    private List<QuestioningEvent> questioningEvents;

    public Upload() {
        super();
    }

    public Upload(Long id, Long date, List<QuestioningEvent> questioningEvents) {
        super();
        this.id = id;
        this.date = date;
        this.questioningEvents = questioningEvents;
    }
}