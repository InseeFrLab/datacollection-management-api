package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Campaign {

    @Id
    private String campaignId;

    private int year;
    private String campaignWording;
    private String period;

    @OneToMany
    private Set<Partitioning> partitionings;

    @OneToOne
    private Survey survey;

}
