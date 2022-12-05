package fr.insee.survey.datacollectionmanagement.questioning.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;

public interface SurveyUnitService {

    public SurveyUnit findbyId(String idSu);

    public List<SurveyUnit> findbyIdentificationCode(String identificationCode);

    public List<SurveyUnit> findbyIdentificationName(String identificationName);

    public Page<SurveyUnit> findAll(Pageable pageable);

    public SurveyUnit saveSurveyUnit(SurveyUnit surveyUnit);
    
    public SurveyUnit saveSurveyUnitAndAddress(SurveyUnit surveyUnit);

    public void deleteSurveyUnit(String id);

}
