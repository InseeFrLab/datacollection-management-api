package fr.insee.survey.datacollectionmanagement.metadata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;

public interface SourceService {

    Source findbyId(String source);

    Page<Source> findAll(Pageable pageable);

    Source updateSource(Source source);

}
