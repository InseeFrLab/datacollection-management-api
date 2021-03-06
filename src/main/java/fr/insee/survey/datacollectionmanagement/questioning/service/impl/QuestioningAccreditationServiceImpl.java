package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningAccreditationRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;

@Service
public class QuestioningAccreditationServiceImpl implements QuestioningAccreditationService {

    @Autowired
    private QuestioningAccreditationRepository questioningAccreditationRepository;

    public List<QuestioningAccreditation> findByIdContact(String id) {
        return questioningAccreditationRepository.findByIdContact(id);
    }

    public Set<QuestioningAccreditation> findBySurveyUnit(SurveyUnit su) {
        Set<QuestioningAccreditation> setReturn = new HashSet<>();
        for (Questioning qu : su.getQuestionings()) {
            setReturn.addAll(qu.getQuestioningAccreditations());
        }
        return setReturn;
    }

    @Override
    public List<String> findIdContactsByPartitionigAccredications(String idPartitioning) {
        return questioningAccreditationRepository.findIdContactsByPartitionigAccredications(idPartitioning);
    }

    @Override
    public List<String> findIdPartitioningsByContactAccreditations(String idContact) {
        return questioningAccreditationRepository.findIdPartitioningsByContactAccreditations(idContact);
    }

    @Override
    public List<String> findIdContactsByIdSource(String idSource) {
        return questioningAccreditationRepository.findIdContactsByIdSource(idSource);
    }

    @Override
    public List<String> findIdContactsByYear(Integer year) {
        return questioningAccreditationRepository.findIdContactsByYear(year);
    }

    @Override
    public List<String> findIdContactsByPeriod(String period) {
        return questioningAccreditationRepository.findIdContactsByPeriod(period);
    }

    @Override
    public List<String> findIdContactsBySourceYearPeriod(String source, Integer year, String period) {
        return questioningAccreditationRepository.findIdContactsBySourceYearPeriod(source, year, period);
    }
}
