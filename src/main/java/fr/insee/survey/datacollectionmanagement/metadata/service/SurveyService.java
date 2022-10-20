package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;

public interface SurveyService {

    List<Survey> findByYear(int year);

    Optional<Survey> findById(String id);

    Page<Survey> findAll(Pageable pageable);

    Survey insertOrUpdateSurvey(Survey survey);

    void deleteSurveyById(String id);

}
