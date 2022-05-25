package fr.insee.survey.datacollectionmanagement.questioning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningAccreditationRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.impl.QuestioningAccreditationService;

@Service
public class QuestioningAccreditationServiceImpl implements QuestioningAccreditationService {

    @Autowired
    private QuestioningAccreditationRepository questioningAccreditationRepository;

    public List<QuestioningAccreditation> findByIdContact(String id) {
        return questioningAccreditationRepository.findByIdContact(id);
    }

}
