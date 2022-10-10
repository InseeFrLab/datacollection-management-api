package fr.insee.survey.datacollectionmanagement.contact.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    @Override
    public String toString() {
        return "ContactEvent [id=" + id + ", eventDate=" + eventDate + ", type=" + type.name()
                + ", payload=" + payload.toString() + "]";
    }

}
