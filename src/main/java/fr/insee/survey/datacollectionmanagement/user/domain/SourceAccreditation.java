package fr.insee.survey.datacollectionmanagement.user.domain;


import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class SourceAccreditation {

    @Id
    @GeneratedValue
    private Long id;

    private Date creationDate;
    private String creationAuthor;
    @NonNull
    private String idUser;

    @OneToOne
    @EqualsAndHashCode.Exclude
    private Source source;

    @Override
    public String toString() {
        return "SourceAccreditation{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", creationAuthor='" + creationAuthor + '\'' +
                ", idUser='" + idUser + '\'' +
                ", source=" + source +
                '}';
    }
}
