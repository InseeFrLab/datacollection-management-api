package fr.insee.survey.datacollectionmanagement.questioning.service;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;

public interface SurveyUnitService {

    public SurveyUnit findbyId(String idSu);

    public List<SurveyUnit> findbySurveyUnitId(String surveyUnitId);

    public List<SurveyUnit> findbyCompanyName(String companyName);
    
    public List<String> findIdContactbySurveyUnitId(String surveyUnitId);
    
    public List<String> findIdContactsByIdSu(String idSu);

    public List<String> findIdContactsbyCompanyName(String companyName);



}
