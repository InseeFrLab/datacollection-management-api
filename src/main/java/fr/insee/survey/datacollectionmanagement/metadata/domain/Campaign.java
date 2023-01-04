package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import fr.insee.survey.datacollectionmanagement.metadata.util.PeriodEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "year_index", columnList = "year_value"),
        @Index(name = "surveyid_index", columnList = "survey_id")
})
public class Campaign {

    @Id
    private String id;
    
    @Column(name = "YEAR_VALUE")
    @NonNull 
    private Integer year;
    
    @Column(name = "PERIOD_VALUE")
    @NonNull
    @Enumerated(EnumType.STRING)
    private PeriodEnum period;
    
    private String campaignWording;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Partitioning> partitionings;

    @OneToOne
    private Survey survey;

}
