package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;

public interface PartitioningService {

    Optional<Partitioning> findById(String id);

    Page<Partitioning> findAll(Pageable pageable);

    Partitioning insertOrUpdatePartitioning(Partitioning partitioning);

    void deletePartitioningById(String id);

}
