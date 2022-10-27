package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SurveyRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Override
    public List<Survey> findByYear(int year) {
        return surveyRepository.findByYear(year);
    }

    @Override
    public Optional<Survey> findById(String id) {
        return surveyRepository.findById(id);
    }

    @Override
    public Page<Survey> findAll(Pageable pageable) {
        return surveyRepository.findAll(pageable);
    }

    @Override
    public Survey insertOrUpdateSurvey(Survey survey) {
        Optional<Survey> surveyBase = findById(survey.getId());
        if (!surveyBase.isPresent()) {
            log.info("Create survey with the id {}", survey.getId());
            return surveyRepository.save(survey);
        }
        log.info("Update survey with the id {}", survey.getId());
        survey.setCampaigns(surveyBase.get().getCampaigns());
        return surveyRepository.save(survey);
    }

    @Override
    public void deleteSurveyById(String id) {
        surveyRepository.deleteById(id);
    }

}
