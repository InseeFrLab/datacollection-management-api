package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.repository.PartitioningRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;

@Service
public class PartioningServiceImpl implements PartitioningService {

    @Autowired
    private PartitioningRepository partitioningRepository;

    @Override
    public Partitioning findById(String id) {
        return partitioningRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Partitioning not found"));

    }

    @Override
    public List<String> findIdPartitioningsBySourceIdYearPeriod(String sourceId, String year, String period) {
        return partitioningRepository.findIdPartitioningBySourceIdYearPeriod(sourceId, year, period);
    }

    @Override
    public List<String> findIdPartitioningsBySourceId(String sourceId) {
        return partitioningRepository.findIdPartitioningBySourceId(sourceId);
    }

    @Override
    public List<String> findIdPartitioningsByYear(String year) {
        return partitioningRepository.findIdPartitioningByYear(year);
    }

    @Override
    public List<String> findIdPartitioningsByPeriod(String period) {
        return partitioningRepository.findIdPartitioningByPeriod(period);
    }

    @Override
    public Page<Partitioning> findAll(Pageable pageable) {
        return partitioningRepository.findAll(pageable);
    }

    @Override
    public Partitioning updatePartitioning(Partitioning partitioning) {
        return partitioningRepository.save(partitioning);

    }

}
