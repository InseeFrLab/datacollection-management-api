package fr.insee.survey.datacollectionmanagement.user.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class UserEvent {

    public enum UserEventType {
        create, update, delete
    }

    @Id
    @GeneratedValue
    private Long id;

    private Date eventDate;
    @NonNull
    private UserEventType type;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    @Override
    public String toString() {
        return "UserEvent{" +
                "id=" + id +
                ", eventDate=" + eventDate +
                ", type=" + type +
                ", user=" + user +
                ", payload=" + payload +
                '}';
    }
}
