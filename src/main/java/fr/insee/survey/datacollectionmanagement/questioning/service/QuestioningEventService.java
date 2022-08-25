package fr.insee.survey.datacollectionmanagement.questioning.service;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;

@Service
public interface QuestioningEventService {

    QuestioningEvent getLastQuestioningEvent(Questioning questioning);
}