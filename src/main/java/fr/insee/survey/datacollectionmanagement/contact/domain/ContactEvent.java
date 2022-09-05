package fr.insee.survey.datacollectionmanagement.contact.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ContactEvent {

    public enum ContactEventType {
        create, update, merged
    }

    @Id
    @GeneratedValue
    private Long id;

    private Date eventDate;

    @ManyToOne
    @JsonBackReference
    private Contact contact;
    private ContactEventType type;

}
