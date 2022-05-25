package fr.insee.survey.datacollectionmanagement.contact.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Contact {

    public enum Gender {
        Female, Male, Undefined
    }

    @Id
    private String identifier;

    private String lastName;
    private String firstName;
    private String email;
    private String function;
    private String phone;
    private String comment;

    @OneToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private Gender gender;


}
