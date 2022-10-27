package fr.insee.survey.datacollectionmanagement.questioning.service;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;

@Service
public interface QuestioningEventService {
    

    public QuestioningEvent findbyId(Long id);

    public QuestioningEvent saveQuestioningEvent(QuestioningEvent questioningEvent);

    public void deleteQuestioningEvent(Long id);

    QuestioningEvent getLastQuestioningEvent(Questioning questioning);
}
