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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(indexes = {
        @Index(name = "fn_index", columnList = "firstName"), @Index(name = "ln_index", columnList = "lastName"),
        @Index(name = "lnfn_index", columnList = "lastName, firstName"),
        @Index(name = "email_index", columnList = "email")
})
@Data
public class Contact {

    public enum Gender {
        Female, Male, Undefined
    }

    @Id
    private String identifier;
    
    private String externalId;
    private String lastName;
    private String firstName;
    private String email;
    private String function;
    private String phone;
    private String comment;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ContactEvent> contactEvents;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
