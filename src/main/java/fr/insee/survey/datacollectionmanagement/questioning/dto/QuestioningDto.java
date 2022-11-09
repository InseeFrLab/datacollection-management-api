package fr.insee.survey.datacollectionmanagement.questioning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestioningDto {

    private Long id;

    private String surveyUnitId;
    private String idPartitioning;
    private String modelName;

}
