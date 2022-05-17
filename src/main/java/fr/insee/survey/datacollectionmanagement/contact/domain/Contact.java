package fr.insee.survey.datacollectionmanagement.contact.domain;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Contact {

    public enum Gender {
        female, male, undefined
    }

    @Id
    private String identifier;

    private String name;
    private String email;
    private String function;
    private String phone;
    private String comment;
    private String zipCode;

    @OneToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany
    private Set<QuestioningAccreditation> questioningAccreditations;

}
