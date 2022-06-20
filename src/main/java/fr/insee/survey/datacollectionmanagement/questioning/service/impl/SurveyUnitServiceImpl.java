package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.repository.SurveyUnitRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;

@Service
public class SurveyUnitServiceImpl implements SurveyUnitService {

    @Autowired
    private SurveyUnitRepository surveyUnitRepository;

    @Override
    public SurveyUnit findbyId(String idSu) {
        return surveyUnitRepository.findById(idSu).orElse(null);
    }

    @Override
    public List<SurveyUnit> findbySurveyUnitId(String surveyUnitId) {
        return surveyUnitRepository.findAllBySurveyUnitId(surveyUnitId);
    }

    @Override
    public List<SurveyUnit> findbyCompanyName(String companyName) {
        return surveyUnitRepository.findByCompanyNameIgnoreCase(companyName);
    }
}
