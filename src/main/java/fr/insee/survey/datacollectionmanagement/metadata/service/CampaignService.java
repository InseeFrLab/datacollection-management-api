package fr.insee.survey.datacollectionmanagement.metadata.service;

import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignMoogDto;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public interface CampaignService {
    Collection<CampaignMoogDto> getCampaigns();
}
