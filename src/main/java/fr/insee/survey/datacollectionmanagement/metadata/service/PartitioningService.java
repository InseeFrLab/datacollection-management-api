package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.Optional;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;

public interface PartitioningService {

    Optional<Partitioning> findById(String id);

    Partitioning insertOrUpdatePartitioning(Partitioning partitioning);

    void deletePartitioningById(String id);

}
