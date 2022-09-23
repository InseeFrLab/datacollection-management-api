package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;

public interface SurveyService {

    List<Survey> findbyYear(int year);

    Survey findbyId(String id);

    Page<Survey> findAll(Pageable pageable);

    Survey updateSurvey(Survey survey);

}
