package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignMoogDto;

@Service
public interface CampaignService {
    
    Collection<CampaignMoogDto> getCampaigns();

    Optional<Campaign> findById(String idCampaign);

    List<Campaign> findbyPeriod(String period);

    List<Campaign> findbySourceYearPeriod(String source, Integer year, String period);

    List<Campaign> findbySourcePeriod(String source, String period);

    Page<Campaign> findAll(Pageable pageable);

    Campaign insertOrUpdateCampaign(Campaign campaign);

    void deleteCampaignById(String id);

    Campaign addPartitionigToCampaign(Campaign campaign, Partitioning partitioning);

}
