package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class QuestioningAccreditation {

    @Id
    @GeneratedValue
    private Long id;

    private boolean isMain;
    private Date creationDate;
    private String creationAuthor;
    private String idContact;

    @OneToOne
    private Questioning questioning;

}
