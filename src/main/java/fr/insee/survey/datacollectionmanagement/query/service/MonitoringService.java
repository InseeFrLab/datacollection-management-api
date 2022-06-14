package fr.insee.survey.datacollectionmanagement.query.service;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogFollowUpDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogProgressDto;
import org.springframework.stereotype.Service;

@Service
public interface MonitoringService {
    JSONCollectionWrapper<MoogProgressDto> getProgress(String idCampaign);

    JSONCollectionWrapper<MoogFollowUpDto> getFollowUp(String idCampaign);
}
