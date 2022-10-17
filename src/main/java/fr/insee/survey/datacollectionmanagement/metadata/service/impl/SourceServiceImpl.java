package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SourceRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;

@Service
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceRepository sourceRepository;

    public Source findById(String source) {
        return sourceRepository.findById(source).orElseThrow();
    }

    @Override
    public Page<Source> findAll(Pageable pageable) {
        return sourceRepository.findAll(pageable);
    }

    @Override
    public Source updateSource(Source source) {
        return sourceRepository.save(source);
    }

    @Override
    public void deleteSourceById(String id) {
        sourceRepository.deleteById(id);

    }

}
