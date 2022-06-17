package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;

public interface SurveyService {

    List<Survey> findbyYear(int year);

}
