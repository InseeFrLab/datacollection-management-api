package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
public class CampaignEvent {

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String type;
    @NonNull
    private Date date;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Campaign campaign;

}
