package fr.insee.survey.datacollectionmanagement.questioning.dto;

import fr.insee.survey.datacollectionmanagement.query.dto.MoogUploadQuestioningEventDto;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UploadDto {

    private List<MoogUploadQuestioningEventDto> data;

}
