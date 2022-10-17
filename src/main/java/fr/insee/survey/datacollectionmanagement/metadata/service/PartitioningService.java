package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;

public interface PartitioningService {

    Partitioning findById(String id);

    List<String> findIdPartitioningsBySourceIdYearPeriod(String sourceId, String year, String period);

    List<String> findIdPartitioningsBySourceId(String sourceId);

    List<String> findIdPartitioningsByYear(String year);

    List<String> findIdPartitioningsByPeriod(String period);

    Page<Partitioning> findAll(Pageable pageable);

    Partitioning updatePartitioning(Partitioning partitioning);

    void deletePartitioningById(String id);

}
