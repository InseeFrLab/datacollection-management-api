package fr.insee.survey.datacollectionmanagement.metadata.service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;

import java.util.List;

public interface PartitioningService {

    Partitioning findById(String id);

    /**
     * Gives the wording of the campain with idSource year and period
     * @param part
     * @return
     */
    String getCampaignWording(Partitioning part);

}
