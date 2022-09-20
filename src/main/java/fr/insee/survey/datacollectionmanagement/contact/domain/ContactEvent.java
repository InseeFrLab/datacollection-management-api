package fr.insee.survey.datacollectionmanagement.contact.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

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
    @Column(columnDefinition = "VARCHAR2(1000) CONSTRAINT IS_VALID_JSON CHECK (payload IS JSON)")
    private String payload;

}
