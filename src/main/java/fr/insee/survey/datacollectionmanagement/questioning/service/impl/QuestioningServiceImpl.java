package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;

@Service
public class QuestioningServiceImpl implements QuestioningService {

    @Autowired
    private QuestioningRepository questioningRepository;

    @Override
    public List<Questioning> fingByIdPartitioning(String idPartitioning) {
        return questioningRepository.findByIdPartitioning( idPartitioning);
    }

}
