package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SurveyRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyrepository;

    @Override
    public List<Survey> findbyYear(int year) {
        return surveyrepository.findByYear(year);
    }

    @Override
    public Survey findbyId(String id) {
        return surveyrepository.findById(id).orElseThrow();
    }

    @Override
    public Page<Survey> findAll(Pageable pageable) {
        return surveyrepository.findAll(pageable);
    }

    @Override
    public Survey updateSurvey(Survey survey) {
        return surveyrepository.save(survey);
    }

}
