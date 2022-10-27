package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Owner;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.repository.OwnerRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.OwnerService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    public Optional<Owner> findById(Long owner) {
        return ownerRepository.findById(owner);
    }

    @Override
    public Page<Owner> findAll(Pageable pageable) {
        return ownerRepository.findAll(pageable);
    }

    @Override
    public Owner insertOrUpdateOwner(Owner owner) {
        Optional<Owner> ownerBase = findById(owner.getId());
        if (!ownerBase.isPresent()) {
            log.info("Create owner with the id {}", owner.getId());
            return ownerRepository.save(owner);
        }
        log.info("Update owner with the id {}", owner.getId());
        owner.setSources(ownerBase.get().getSources());
        return ownerRepository.save(owner);
    }

    @Override
    public void deleteOwnerById(Long id) {
        ownerRepository.deleteById(id);

    }

    @Override
    public void removeSourceFromOwner(Owner owner, Source source) {
        owner.getSources().remove(source);
        ownerRepository.save(owner);
    }

}
