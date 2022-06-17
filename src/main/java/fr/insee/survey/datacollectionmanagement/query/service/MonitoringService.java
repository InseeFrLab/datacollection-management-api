package fr.insee.survey.datacollectionmanagement.query.service;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogFollowUpDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogProgressDto;

@Service
public interface MonitoringService {
    JSONCollectionWrapper<MoogProgressDto> getProgress(String idCampaign);

    JSONCollectionWrapper<MoogFollowUpDto> getFollowUp(String idCampaign);
}
