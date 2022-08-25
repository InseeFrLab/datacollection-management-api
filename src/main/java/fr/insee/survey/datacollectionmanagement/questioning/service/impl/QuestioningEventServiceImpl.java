package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.util.TypeQuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;

@Service
public class QuestioningEventServiceImpl implements QuestioningEventService {

    @Override
    public QuestioningEvent getLastQuestioningEvent(Questioning questioning) {
        Set<QuestioningEvent> setQuestioningEvent = questioning.getQuestioningEvents();
        List<QuestioningEvent> setQuestioningEventsAnswer =
            setQuestioningEvent.stream()
                .filter(
                    qe -> qe.getType().equals(TypeQuestioningEvent.PARTIELINT.name()) || qe.getType().equals(TypeQuestioningEvent.VALINT.name()))
                .collect(Collectors.toList());
        Collections.sort(setQuestioningEventsAnswer, Comparator.comparing(QuestioningEvent::getType));
        return setQuestioningEventsAnswer.stream().findFirst().orElseThrow();
    }

}
