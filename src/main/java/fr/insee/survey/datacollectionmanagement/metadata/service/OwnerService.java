package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Owner;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;

public interface OwnerService {

    Optional<Owner> findById(String owner);

    Page<Owner> findAll(Pageable pageable);

    Owner insertOrUpdateOwner(Owner owner);

    void deleteOwnerById(String id);

    void removeSourceFromOwner(Owner owner, Source source);
    
    void addSourceFromOwner(Owner owner, Source source);

}
