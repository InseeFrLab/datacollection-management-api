package fr.insee.survey.datacollectionmanagement.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccreditationDetailDto {

    private String sourceId;
    private String sourceWording;
    private int year;
    private String period;
    private String partition;
    private String surveyUnitId;
    private String companyName;
    private boolean isMain;

    public AccreditationDetailDto(
        String sourceId,
        String sourceWording,
        int year,
        String period,
        String partition,
        String surveyUnitId,
        String companyName,
        boolean isMain) {
        super();
        this.sourceId = sourceId;
        this.sourceWording = sourceWording;
        this.year = year;
        this.period = period;
        this.partition = partition;
        this.surveyUnitId = surveyUnitId;
        this.companyName = companyName;
        this.isMain = isMain;
    }

}
