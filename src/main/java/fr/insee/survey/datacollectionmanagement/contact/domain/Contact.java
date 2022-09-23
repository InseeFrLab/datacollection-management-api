package fr.insee.survey.datacollectionmanagement.contact.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(indexes = {
    @Index(name = "fn_index", columnList = "firstName"), @Index(name = "ln_index", columnList = "lastName"),
    @Index(name = "lnfn_index", columnList = "lastName, firstName"), @Index(name = "email_index", columnList = "email")
})
@Getter
@Setter
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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ContactEvent> contactEvents;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
