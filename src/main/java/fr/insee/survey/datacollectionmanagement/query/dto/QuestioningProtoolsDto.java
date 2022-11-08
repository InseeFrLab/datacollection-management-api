package fr.insee.survey.datacollectionmanagement.query.dto;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.questioning.dto.SurveyUnitDto;
import lombok.Data;

@Data
public class QuestioningProtoolsDto {

    private String idPartitioning;
    private String modelName;
    private SurveyUnitDto surveyUnit;
    private List<ContactAccreditationDto> contacts;

}
