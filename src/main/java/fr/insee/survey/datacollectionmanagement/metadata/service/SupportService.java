package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Support;

public interface SupportService {

    Optional<Support> findById(Long support);

    Page<Support> findAll(Pageable pageable);

    Support insertOrUpdateSupport(Support support);

    void deleteSupportById(Long id);

    void removeSourceFromSupport(Support support, Source source);

}
