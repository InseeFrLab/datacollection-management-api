package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class CampaignEvent {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String type;
    @NotNull
    private Date date;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Campaign campaign;

}
