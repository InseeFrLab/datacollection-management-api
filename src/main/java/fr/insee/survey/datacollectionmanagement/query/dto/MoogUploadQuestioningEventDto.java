package fr.insee.survey.datacollectionmanagement.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoogUploadQuestioningEventDto {

    private String idSu;
    private String idContact;
    private String date;
    private String status;
}
