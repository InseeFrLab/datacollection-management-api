package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.util.PartitioningStatusEnum;

public interface PartitioningService {

    Optional<Partitioning> findById(String id);

    Partitioning insertOrUpdatePartitioning(Partitioning partitioning);

    void deletePartitioningById(String id);
    
    PartitioningStatusEnum calculatePartitioningStatus(Partitioning partitioning);

    Date calculatePartitioningDate(Partitioning part, PartitioningStatusEnum partitioningStatusEnum);

}
