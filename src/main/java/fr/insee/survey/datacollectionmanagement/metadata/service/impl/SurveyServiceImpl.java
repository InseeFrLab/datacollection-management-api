package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

}
