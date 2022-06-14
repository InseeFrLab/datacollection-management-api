package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SourceRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;

@Service
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceRepository sourceRepository;

    public Source findbyId(String source) {
        return sourceRepository.findById(source).orElse(null);
    }

}
