package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.EventOrder;
import fr.insee.survey.datacollectionmanagement.questioning.repository.EventOrderRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.EventOrderService;

@Service
public class EventOrderServiceImpl implements EventOrderService {

    static final Logger LOGGER = LoggerFactory.getLogger(EventOrderServiceImpl.class);

    @Autowired
    EventOrderRepository eventOrderRepository;

    public EventOrder saveAndFlush(EventOrder order) {
        return eventOrderRepository.saveAndFlush(order);
    }

    @Override
    public EventOrder findByStatus(String status) {
        return eventOrderRepository.findByStatus(status);
    }

}
