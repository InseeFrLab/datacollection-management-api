package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String getCampaignWording(Partitioning part) {
        return part.getCampaign().getSurvey().getSource().getIdSource() + " " + part.getCampaign().getSurvey().getYear() + " " + part.getCampaign().getPeriod();
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

}
