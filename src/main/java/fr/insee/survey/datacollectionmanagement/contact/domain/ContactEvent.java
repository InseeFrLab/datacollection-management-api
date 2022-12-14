package fr.insee.survey.datacollectionmanagement.contact.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ContactEvent {

    public enum ContactEventType {
        create, update, merged, firstConnect
    }

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private Date eventDate;
    @NotNull
    private ContactEventType type;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Contact contact;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    @Override
    public String toString() {
        return "ContactEvent [id=" + id + ", eventDate=" + eventDate + ", type=" + type.name()
                + ", payload=" + payload.toString() + "]";
    }

}
