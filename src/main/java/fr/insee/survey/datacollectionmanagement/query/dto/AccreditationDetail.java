package fr.insee.survey.datacollectionmanagement.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccreditationDetail {

    private String surveyId;
    private int year;
    private String period;
    private String surveyUnitId;

    public AccreditationDetail(String surveyId, int year, String period, String surveyUnitId) {
        super();
        this.surveyId = surveyId;
        this.year = year;
        this.period = period;
        this.surveyUnitId = surveyUnitId;
    }

}
