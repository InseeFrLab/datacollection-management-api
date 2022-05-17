package fr.insee.survey.datacollectionmanagement.contact.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
