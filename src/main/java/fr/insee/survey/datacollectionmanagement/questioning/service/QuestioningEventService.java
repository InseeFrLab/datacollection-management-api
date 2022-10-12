package fr.insee.survey.datacollectionmanagement.questioning.service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;

public interface QuestioningEventService {

    public QuestioningEvent findbyId(Long id);

    public QuestioningEvent saveQuestioningEvent(QuestioningEvent questioningEvent);

    public void deleteQuestioningEvent(Long id);

}
