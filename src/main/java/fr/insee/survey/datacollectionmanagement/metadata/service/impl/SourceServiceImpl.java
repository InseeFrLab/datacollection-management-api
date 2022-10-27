package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SourceRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceRepository sourceRepository;

    public Optional<Source> findById(String source) {
        return sourceRepository.findById(source);
    }

    @Override
    public Page<Source> findAll(Pageable pageable) {
        return sourceRepository.findAll(pageable);
    }

    @Override
    public Source insertOrUpdateSource(Source source) {
        Optional<Source> sourceBase = findById(source.getId());
        if(!sourceBase.isPresent()) {
            log.info("Create source with the id {}", source.getId());
            return sourceRepository.save(source);
        }
        log.info("Update source with the id {}", source.getId());
        source.setSurveys(sourceBase.get().getSurveys());
        return sourceRepository.save(source);
    }

    @Override
    public void deleteSourceById(String id) {
        sourceRepository.deleteById(id);

    }

}
