package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;

public interface SourceService {

    Optional<Source> findById(String source);

    Page<Source> findAll(Pageable pageable);

    Source insertOrUpdateSource(Source source);

    void deleteSourceById(String id);

    Source addSurveyToSource(Source source, Survey survey);

}
