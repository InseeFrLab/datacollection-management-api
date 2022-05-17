package fr.insee.survey.datacollectionmanagement.questioning.domain;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class QuestioningAccreditation {

    @Id
    @GeneratedValue
    private Long id;

    private boolean isMain;
    private Date creationDate;
    private String creationAuthor;

}
