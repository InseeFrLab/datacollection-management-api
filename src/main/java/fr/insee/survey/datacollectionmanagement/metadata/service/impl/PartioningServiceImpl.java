package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.repository.PartitioningRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PartioningServiceImpl implements PartitioningService {

    @Autowired
    private PartitioningRepository partitioningRepository;

    @Override
    public Optional<Partitioning> findById(String id) {
        return partitioningRepository.findById(id);

    }

    @Override
    public Page<Partitioning> findAll(Pageable pageable) {
        return partitioningRepository.findAll(pageable);
    }

    @Override
    public Partitioning insertOrUpdatePartitioning(Partitioning partitioning) {
        Optional<Partitioning> campaignBase = findById(partitioning.getId());
        if (!campaignBase.isPresent()) {
            log.info("Create campaign with the id {}", partitioning.getId());
            return partitioningRepository.save(partitioning);
        }
        log.info("Update campaign with the id {}", partitioning.getId());
        return partitioningRepository.save(partitioning);

    }

    @Override
    public void deletePartitioningById(String id) {
        partitioningRepository.deleteById(id);
    }

}
