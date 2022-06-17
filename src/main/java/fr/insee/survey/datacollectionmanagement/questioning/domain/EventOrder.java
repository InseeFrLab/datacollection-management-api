package fr.insee.survey.datacollectionmanagement.questioning.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class EventOrder {

    @Id
    private Long id;

    private String status;

    private int eventOrder;

    public EventOrder(long id, String status, int eventOrder) {
        super();
        this.id = id;
        this.status = status;
        this.eventOrder = eventOrder;
    }

    public EventOrder() {
    }
}
