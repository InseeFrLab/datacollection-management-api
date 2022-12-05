package fr.insee.survey.datacollectionmanagement.query.dto;

import fr.insee.survey.datacollectionmanagement.metadata.util.PeriodEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccreditationDetailDto {

    private String sourceId;
    private String sourceWording;
    private int year;
    private PeriodEnum period;
    private String partition;
    private String identificationCode;
    private String identificationName;
    private boolean isMain;

    public AccreditationDetailDto(
        String sourceId,
        String sourceWording,
        int year,
        PeriodEnum period,
        String partition,
        String identificationCode,
        String identificationName,
        boolean isMain) {
        super();
        this.sourceId = sourceId;
        this.sourceWording = sourceWording;
        this.year = year;
        this.period = period;
        this.partition = partition;
        this.identificationCode = identificationCode;
        this.identificationName = identificationName;
        this.isMain = isMain;
    }

}
