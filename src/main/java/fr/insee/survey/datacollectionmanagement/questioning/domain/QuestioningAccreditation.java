package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idContact_index", columnList = "idContact"),
        @Index(name = "questioning_index", columnList = "questioning_id")
})
public class QuestioningAccreditation {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private Boolean isMain;
    private Date creationDate;
    private String creationAuthor;
    @NonNull
    private String idContact;

    @OneToOne
    @EqualsAndHashCode.Exclude
    private Questioning questioning;

    @Override
    public String toString() {
        return "QuestioningAccreditation [id=" + id + ", isMain=" + isMain + ", creationDate=" + creationDate
                + ", creationAuthor=" + creationAuthor + ", idContact=" + idContact + "]";
    }

}
