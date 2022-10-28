package fr.insee.survey.datacollectionmanagement.questioning.util;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.insee.survey.datacollectionmanagement.questioning.domain.EventOrder;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.service.EventOrderService;

@Component
public class LastQuestioningEventComparator implements Comparator<QuestioningEvent> {

    @Autowired
    private EventOrderService eventOrderService;

    @Override
    public int compare(QuestioningEvent o1, QuestioningEvent o2) {
        EventOrder eventOrder1 = eventOrderService.findByStatus(o1.getType().name());
        EventOrder eventOrder2 = eventOrderService.findByStatus(o2.getType().name());

        return Integer.compare(eventOrder2.getEventOrder(), eventOrder1.getEventOrder());
    }

}
