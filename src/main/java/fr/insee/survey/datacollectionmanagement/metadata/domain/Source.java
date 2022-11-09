package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Source {

    @Id
    private String id;
    private String longWording;
    private String shortWording;
    @NotNull
    private String periodicity;
    @NotNull
    private boolean mandatoryMySurveys;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Survey> surveys;

    @OneToOne
    @NotNull
    private Owner owner;

    @OneToOne
    @NotNull
    private Support support;

}
