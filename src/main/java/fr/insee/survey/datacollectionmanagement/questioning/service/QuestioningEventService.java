package fr.insee.survey.datacollectionmanagement.questioning.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.util.TypeQuestioningEvent;

@Service
public interface QuestioningEventService {

    public Optional<QuestioningEvent> findbyId(Long id);

    public QuestioningEvent saveQuestioningEvent(QuestioningEvent questioningEvent);

    public void deleteQuestioningEvent(Long id);

    public void deleteAll(List<QuestioningEvent> questioningEvents);

    /**
     * Get the last event sorted by order of importance among the event types
     * (TypeQuestioningEvent) passed in parameter
     * 
     * @param questioning
     * @param events      list of events to be considered
     * @return optional last Questioning event in order of importance
     */
    Optional<QuestioningEvent> getLastQuestioningEvent(Questioning questioning, List<TypeQuestioningEvent> events);
}
