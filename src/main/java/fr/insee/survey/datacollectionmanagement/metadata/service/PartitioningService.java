package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.Date;
import java.util.List;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.util.PartitioningStatusEnum;

public interface PartitioningService {
    
    Partitioning findById(String id);

    List<String> findIdPartitioningsBySourceIdYearPeriod(String sourceId, String year, String period);

    List<String> findIdPartitioningsBySourceId(String sourceId);

    List<String> findIdPartitioningsByYear(String year);

    List<String> findIdPartitioningsByPeriod(String period);
    
    PartitioningStatusEnum calculatePartitioningStatus(Partitioning partitioning);

    Date calculatePartitioningDate(Partitioning part, PartitioningStatusEnum partitioningStatusEnum);

}
