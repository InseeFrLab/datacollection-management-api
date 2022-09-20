package fr.insee.survey.datacollectionmanagement.contact.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
    @Index(name = "ac_sourceId_index", columnList = "sourceId"), @Index(name = "ac_year_index", columnList = "YEAR_VALUE"),
    @Index(name = "ac_period_index", columnList = "PERIOD_VALUE"), @Index(name = "ac_idSu_index", columnList = "idSu"),
    @Index(name = "ac_surveyUnitId_index", columnList = "surveyUnitId"), @Index(name = "ac_companyName_index", columnList = "companyName")
})
public class AccreditationsCopy {

    @Id
    @GeneratedValue
    private Long id;
    private String sourceId;
    @Column(name = "YEAR_VALUE")
    private Integer year;
    @Column(name = "PERIOD_VALUE")
    private String period;
    private String idSu;
    private String surveyUnitId;
    private String companyName;

    @ManyToOne
    private Contact contact;

}
