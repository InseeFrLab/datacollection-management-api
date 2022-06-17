package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.repository.PartitioningRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.PartitioningService;

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

}
