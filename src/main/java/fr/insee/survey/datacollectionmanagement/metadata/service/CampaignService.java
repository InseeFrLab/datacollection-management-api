package fr.insee.survey.datacollectionmanagement.metadata.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignMoogDto;

@Service
public interface CampaignService {
    Collection<CampaignMoogDto> getCampaigns();

    List<Campaign> findbyPeriod(String period);

    Campaign findById(String idCampaign);
}
