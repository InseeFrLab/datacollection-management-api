package fr.insee.survey.datacollectionmanagement.questioning.service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.EventOrder;

public interface EventOrderService {

    EventOrder findByStatus(String status);
}
