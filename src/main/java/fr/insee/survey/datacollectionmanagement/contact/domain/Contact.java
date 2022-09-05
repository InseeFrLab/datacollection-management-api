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

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String campaignId;

    private String lastName;
    private String firstName;
    private String email;
    private String function;
    private String phone;
    private String comment;

    @OneToOne
    @JsonIgnore
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, targetEntity=ContactEvent.class, cascade = CascadeType.ALL, mappedBy="contact" )
    @JsonIgnore
    private Set<ContactEvent> contactEvents;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
