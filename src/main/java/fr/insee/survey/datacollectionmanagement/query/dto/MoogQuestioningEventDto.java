package fr.insee.survey.datacollectionmanagement.query.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoogQuestioningEventDto implements Serializable {

    private String idManagementMonitoringInfo;
    private MoogSearchDto surveyUnit;
    private String status;
    private String upload;
    private Long dateInfo;
}
