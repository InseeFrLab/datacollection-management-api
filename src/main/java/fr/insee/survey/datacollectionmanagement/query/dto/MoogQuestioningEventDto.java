package fr.insee.survey.datacollectionmanagement.query.dto;

import fr.insee.survey.datacollectionmanagement.questioning.domain.TypeQuestioningEvent;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MoogQuestioningEventDto implements Serializable {

    private String idManagementMonitoringInfo;
    private MoogSearchDto surveyUnit;
    private String status;
    private String upload;
    private Long dateInfo;
}
