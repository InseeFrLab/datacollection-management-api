package fr.insee.survey.datacollectionmanagement.questioning.service;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;


public interface PartitioningService {
    
    Partitioning findById(String id);

}
