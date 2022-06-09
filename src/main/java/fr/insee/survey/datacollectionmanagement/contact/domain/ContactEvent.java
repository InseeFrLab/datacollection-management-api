package fr.insee.survey.datacollectionmanagement.contact.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class ContactEvent {

    public enum ContactEventType {
        create, update, merged
    }

    @Id
    @GeneratedValue
    private Long id;

    private Date eventDate;

    @ManyToOne
    private Contact contact;
    private ContactEventType type;

}
