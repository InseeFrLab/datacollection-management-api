package fr.insee.survey.datacollectionmanagement.metadata.service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;

public interface SourceService {

    Source findbyId(String source);

}
