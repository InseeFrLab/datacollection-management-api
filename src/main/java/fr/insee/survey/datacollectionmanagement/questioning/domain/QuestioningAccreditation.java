package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(indexes = {
        @Index(name = "idContact_index", columnList = "idContact"),
        @Index(name = "questioning_index", columnList = "questioning_id")
})
public class QuestioningAccreditation {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private boolean isMain;
    private Date creationDate;
    private String creationAuthor;
    @NotNull
    private String idContact;

    @OneToOne
    private Questioning questioning;

    @Override
    public String toString() {
        return "QuestioningAccreditation [id=" + id + ", isMain=" + isMain + ", creationDate=" + creationDate
                + ", creationAuthor=" + creationAuthor + ", idContact=" + idContact + "]";
    }

}
