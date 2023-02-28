package fr.insee.survey.datacollectionmanagement.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "InternalUsers")
public class User {

    public enum UserRoleType {
        responsable, gestionnaire, assistance
    }


    @Id
    private String identifier;

    private UserRoleType role;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UserEvent> userEvents;

}
