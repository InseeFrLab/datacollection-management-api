package fr.insee.survey.datacollectionmanagement.metadata.domain;

import fr.insee.survey.datacollectionmanagement.metadata.util.PeriodicityEnum;
import fr.insee.survey.datacollectionmanagement.user.domain.SourceAccreditation;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Source {

    @Id
    private String id;
    private String longWording;
    private String shortWording;
    @NonNull
    @Enumerated(EnumType.STRING)
    private PeriodicityEnum periodicity;
    @NonNull
    private Boolean mandatoryMySurveys;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Survey> surveys;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<SourceAccreditation> sourceAccreditations;

    @OneToOne
    @NonNull
    private Owner owner;

    @OneToOne
    @NonNull
    private Support support;

}
