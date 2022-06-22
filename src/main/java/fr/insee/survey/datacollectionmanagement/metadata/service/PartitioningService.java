package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;

public interface PartitioningService {

    Partitioning findById(String id);

    /**
     * Gives the wording of the campain with idSource year and period
     * @param part
     * @return
     */
    String getCampaignWording(Partitioning part);

    List<String> findIdPartitioningsBySourceIdYearPeriod(String sourceId, String year, String period);

    List<String> findIdPartitioningsBySourceId(String sourceId);

    List<String> findIdPartitioningsByYear(String year);

    List<String> findIdPartitioningsByPeriod(String period);

}
