package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;

@Service
public class QuestioningServiceImpl implements QuestioningService {

    @Autowired
    private QuestioningRepository questioningRepository;

    @Override
    public Page<Questioning> findAll(Pageable pageable) {
        return questioningRepository.findAll(pageable);
    }

    @Override
    public Questioning findbyId(Long id) {
        return questioningRepository.findById(id).orElseThrow();
    }

    @Override
    public Questioning updateQuestioning(Questioning questioning) {
        return questioningRepository.save(questioning);
    }

}
