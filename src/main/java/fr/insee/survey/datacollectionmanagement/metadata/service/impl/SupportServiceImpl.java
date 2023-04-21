package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Support;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SupportRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.SupportService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupportServiceImpl implements SupportService {

    @Autowired
    private SupportRepository supportRepository;

    public Optional<Support> findById(String support) {
        return supportRepository.findById(support);
    }

    @Override
    public Page<Support> findAll(Pageable pageable) {
        return supportRepository.findAll(pageable);
    }

    @Override
    public Support insertOrUpdateSupport(Support support) {
        Optional<Support> supportBase = findById(support.getId());
        if (!supportBase.isPresent()) {
            log.info("Create support with the id {}", support.getId());
            return supportRepository.save(support);
        }
        log.info("Update support with the id {}", support.getId());
        support.setSources(supportBase.get().getSources());
        return supportRepository.save(support);
    }

    @Override
    public void deleteSupportById(String id) {
        supportRepository.deleteById(id);

    }

    @Override
    public void removeSourceFromSupport(Support support, Source source) {
        if (support != null && support.getSources() != null) {
            support.getSources().remove(source);
            supportRepository.save(support);
        }
    }

    @Override
    public void addSourceFromSupport(Support support, Source source) {
        support.getSources().add(source);
        supportRepository.save(support);
    }

}
