package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idContact_index", columnList = "idContact"),
        @Index(name = "questioning_index", columnList = "questioning_id")
})
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
