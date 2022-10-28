package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningEventRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;
import fr.insee.survey.datacollectionmanagement.questioning.util.LastQuestioningEventComparator;
import fr.insee.survey.datacollectionmanagement.questioning.util.TypeQuestioningEvent;

@Service
public class QuestioningEventServiceImpl implements QuestioningEventService {

    @Autowired
    LastQuestioningEventComparator lastQuestioningEventComparator;

    @Autowired
    QuestioningEventRepository questioningEventRepository;

    @Override
    public QuestioningEvent findbyId(Long id) {
        return questioningEventRepository.findById(id).orElseThrow();
    }

    @Override
    public QuestioningEvent saveQuestioningEvent(QuestioningEvent questioningEvent) {
        return questioningEventRepository.save(questioningEvent);
    }

    @Override
    public void deleteQuestioningEvent(Long id) {
        questioningEventRepository.deleteById(id);

    }

    @Override
    public QuestioningEvent getLastQuestioningEvent(Questioning questioning) {
        List<QuestioningEvent> listQuestioningEvent = questioning.getQuestioningEvents().stream()
                .filter(qe -> qe.getType().equals(TypeQuestioningEvent.PARTIELINT)
                        || qe.getType().equals(TypeQuestioningEvent.HC)
                        || qe.getType().equals(TypeQuestioningEvent.VALPAP)
                        || qe.getType().equals(TypeQuestioningEvent.VALINT)
                        || qe.getType().equals(TypeQuestioningEvent.REFUSAL))
                .collect(Collectors.toList());
        Collections.sort(listQuestioningEvent, lastQuestioningEventComparator);
        return listQuestioningEvent.stream().findFirst().orElseThrow();
    }

}
