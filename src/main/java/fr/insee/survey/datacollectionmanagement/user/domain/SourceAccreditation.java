package fr.insee.survey.datacollectionmanagement.user.domain;


import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idUser_index", columnList = "idUser"),
        @Index(name = "source_index", columnList = "source_id")
})
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
